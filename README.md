# Sistema Leitor de Placas

Este repositório contém a arquitetura de um sistema para leitura e processamento de placas de veículos, composto por um backend principal em Java com Spring Boot e um módulo dedicado de Processamento Digital de Imagens (PDI) em Python.

## Sumário

1.  [Visão Geral do Projeto](#1-visão-geral-do-projeto)
2.  [Arquitetura do Backend Principal (Java/Spring Boot)](#2-arquitetura-do-backend-principal-javaspring-boot)
    * 2.1. Visão Geral e Stack de Tecnologia
    * 2.2. Estrutura em Camadas
    * 2.3. Módulo de Segurança
    * 2.4. Principais Fluxos e Funcionalidades
3.  [Arquitetura do Módulo de Processamento Digital de Imagens (PDI - Python)](#3-arquitetura-do-módulo-de-processamento-digital-de-imagens-pdi---python)
    * 3.1. Visão Geral e Stack de Tecnologia
    * 3.2. Estrutura em Módulos
    * 3.3. Fluxo de Processamento de Imagem
4.  [Arquitetura do Frontend](#4-arquitetura-do-frontend)
5.  [Estrutura do Repositório](#5-estrutura-do-repositório)
6.  [Como Rodar o Projeto](#6-como-rodar-o-projeto)
    * 6.1. Pré-requisitos
    * 6.2. Configuração e Execução do Módulo PDI (Python)
    * 6.3. Configuração e Execução do Backend (Java)
    * 6.4. Configuração e Execução do Frontend
7.  [Contribuição](#7-contribuição)
8.  [Licença](#8-licença)

---

## 1. Visão Geral do Projeto

O Sistema Leitor de Placas é uma aplicação robusta desenvolvida para detectar, reconhecer e gerenciar informações de placas de veículos. Ele é dividido em três componentes principais: um **Frontend** para interação com o usuário, uma **API RESTful em Java (Spring Boot)** para lógica de negócio, persistência de dados e segurança, e um **módulo especializado em Python (FastAPI)** para todo o pipeline de Visão Computacional (detecção, pré-processamento e OCR de placas). O sistema é projetado para ser "stateless" (sem estado) e utiliza autenticação baseada em tokens JWT.

## 2. Arquitetura do Backend Principal (Java/Spring Boot)

### 2.1. Visão Geral e Stack de Tecnologia

O backend principal é a API RESTful desenvolvida em Java 21 com o framework Spring Boot 3. Ele segue uma arquitetura em camadas e é o responsável pela orquestração das operações, gestão de usuários, veículos e históricos de leitura.

* **Linguagem:** Java 21
* **Framework Principal:** Spring Boot
* **Módulos Spring:** Web, Data JPA, Security, Mail, Validation
* **Persistência de Dados:** Spring Data JPA com Hibernate
* **Banco de Dados:** MySQL (para produção/desenvolvimento) e H2 (para testes)
* **Build e Dependências:** Maven
* **Documentação da API:** Swagger / OpenAPI 3 com `springdoc-openapi`

### 2.2. Estrutura em Camadas

O projeto é organizado em uma arquitetura de camadas clássica para garantir a separação de responsabilidades e a manutenibilidade:

* **Controller (Camada Web):** Ponto de entrada da API. Recebe requisições HTTP, realiza validação superficial de DTOs e delega a lógica de negócio para a camada de Serviço. Responsável por construir e retornar `ResponseEntity` com status HTTP e corpo da resposta.
* **Service (Camada de Serviço):** Contém a lógica de negócio principal, orquestração de processos e regras de validação. É transacional (`@Transactional`) e se comunica com a camada de Repositório para acesso a dados. **Esta camada também orquestra a comunicação com o Módulo PDI (Python)** para o processamento de imagens e obtenção da placa reconhecida.
* **Repository (Camada de Dados):** Interfaces que estendem `JpaRepository`. Abstraem o acesso ao banco de dados, permitindo operações de CRUD e consultas customizadas.
* **Model (Camada de Domínio):** Classes de entidade (`@Entity`) que mapeiam as tabelas do banco de dados, representando os objetos de negócio centrais (Ex: `AppUser`, `Vehicle`, `ScanHistory`).
* **DTO (Data Transfer Object):** Objetos simples (geralmente `record`) que definem o "contrato" de dados da API, usados para transportar dados entre o cliente e o controller, evitando a exposição das entidades do banco de dados.

### 2.3. Módulo de Segurança

A segurança é robusta, stateless e baseada em tokens:

* **Autenticação:** Utiliza fluxo de Access Token + Refresh Token.
    * **Access Token (JWT):** Token de vida curta (ex: 1 hora), assinado com HS256, enviado em cada requisição para acessar endpoints protegidos.
    * **Refresh Token:** Token de vida longa (ex: 7 dias), armazenado em banco de dados, usado exclusivamente para obter novo `accessToken` via `/auth/refresh`.
* **Autorização:** Baseada em papéis (Roles), como `ROLE_ADMIN` e `ROLE_STANDARD`. Regras de permissão aplicadas nos endpoints via `SecurityFilterChain` (`.authorizeHttpRequests()`) e anotações (`@PreAuthorize`).
* **Logout:** Invalidação de tokens via mecanismo de blacklist, onde o token JWT é armazenado até sua expiração para prevenir reuso.
* **Tratamento de Erros de Segurança:** `GlobalExceptionHandler` (`@RestControllerAdvice`), `AuthenticationEntryPoint` e `AccessDeniedHandler` customizados garantem respostas JSON padronizadas (`StandardError`) com códigos 401 Unauthorized ou 403 Forbidden.

### 2.4. Principais Fluxos e Funcionalidades

* **Onboarding de Usuário:** Novos usuários criados exclusivamente por Administrador. Gera token de ativação de uso único enviado por e-mail para o usuário definir senha e ativar conta.
* **Registro de Placa Escaneada (Scan):**
    * Frontend captura a imagem da placa.
    * A imagem é enviada ao **Módulo PDI (Python)**, que retorna a string da placa reconhecida.
    * Essa string é enviada ao endpoint Java (POST `/scans/register`).
    * O backend cria registro de auditoria (`ScanHistory`), associando placa e policial autenticado, e verifica o status do veículo. Resposta rápida (202 Accepted).
* **Consulta de Detalhes do Veículo:** Usuário seleciona placa (do histórico ou recém-escaneada). Frontend envia string da placa para endpoint (GET `/vehicles/{plate}`). Backend busca detalhes no banco de dados e retorna.
* **Consulta de Histórico:** Endpoint paginado (GET `/history/scans`) permite que cada usuário consulte seu próprio histórico de leituras.

## 3. Arquitetura do Módulo de Processamento Digital de Imagens (PDI - Python)

O Módulo PDI é um serviço dedicado, implementado como uma API RESTful em Python utilizando FastAPI. Ele é responsável por todo o pipeline de visão computacional, desde a detecção da placa na imagem até o reconhecimento e correção do texto.

### 3.1. Visão Geral e Stack de Tecnologia

* **Linguagem:** Python
* **Framework de API:** FastAPI
* **Bibliotecas de Visão Computacional/Machine Learning:**
    * `ultralytics`: Para detecção de objetos (YOLOv8 para placas).
    * `PaddleOCR`: Para reconhecimento de texto (OCR).
    * `OpenCV` (`cv2`): Para manipulação e pré-processamento de imagens.
    * `Pillow` (`PIL`): Para carregamento e conversão de imagens.
    * `NumPy`: Para operações numéricas em arrays de imagens.
* **Recursos Adicionais:** Utiliza expressões regulares (`re`) para limpeza e correção de texto.

### 3.2. Estrutura em Módulos

O módulo PDI é organizado em sub-módulos lógicos, refletindo as etapas do pipeline de processamento:

* **`main.py` (Módulo OCR / Endpoint API):**
    * **Função Principal:** Orquestrador do pipeline de processamento e ponto de entrada da API PDI via endpoint `/processar/` (POST).
    * **Fluxo:**
        1.  Recebe imagem (`UploadFile`).
        2.  Chama `IA.bbox_cut` para detecção e recorte da placa.
        3.  Chama `IMP.filters` para pré-processamento da imagem recortada.
        4.  Executa OCR (`PaddleOCR`) na imagem processada.
        5.  Filtra e consolida resultados do OCR.
        6.  Chama `OCR.corretors` para correção e pós-processamento do texto.
        7.  Padroniza o tamanho da placa e retorna o texto final.
    * **Mecanismos:** Gerencia arquivos temporários, tratamento de exceções e logging.

* **`IA` (Inteligência Artificial / Detecção):**
    * **Arquivo:** `bbox_cut.py`
    * **Responsabilidade:** Detectar a localização da placa na imagem e recortá-la.
    * **Tecnologia:** Modelo `YOLOv8` (`best2.pt`).
    * **Funcionalidades:** `recortar_placa(img_bgr, ...)`: Encontra a placa, recorta a região, e tenta rotações automáticas se a placa não for inicialmente detectada. Classifica a placa como "moto" ou "carro" com base nas proporções. Gera imagens de debug.

* **`IMP` (Image Processing / Pré-processamento):**
    * **Arquivo:** `filters.py`
    * **Responsabilidade:** Melhorar a qualidade da imagem da placa para otimizar o reconhecimento OCR.
    * **Tecnologia:** `OpenCV`.
    * **Funcionalidades:** `aplicar_filtros(img)`: Converte para escala de cinza, aplica Gaussian Blur, operações morfológicas (erosão, dilatação) e binarização adaptativa para realçar caracteres e remover ruídos.

* **`OCR` (Optical Character Recognition / Pós-processamento e Correção):**
    * **Arquivo:** `corretors.py`
    * **Responsabilidade:** Corrigir erros comuns de reconhecimento de caracteres do OCR bruto.
    * **Tecnologia:** Python `re`.
    * **Funcionalidades:** Funções como `corrigir_placa_nova()`, `corrigir_placa_antiga()`, `corrigir_placa_moto()`: Aplicam regras específicas de correção baseadas em padrões de placas (Mercosul, antigas, motos) e trocas comuns entre letras e números. Remove caracteres não alfanuméricos e ajusta o formato.

### 3.3. Fluxo de Processamento de Imagem

O fluxo de uma imagem no Módulo PDI é:

1.  **Recebimento:** Imagem enviada ao `/processar/`.
2.  **Detecção:** `IA` localiza e recorta a placa.
3.  **Pré-processamento:** `IMP` aplica filtros na placa recortada.
4.  **Reconhecimento:** `PaddleOCR` extrai o texto bruto.
5.  **Correção:** `OCR` corrige e formata o texto.
6.  **Retorno:** Placa corrigida é enviada como resposta.

## 4. Arquitetura do Frontend

O frontend é a interface do usuário para o Sistema Leitor de Placas, responsável por prover a interação, captura de imagens e exibição de dados.

* **Propósito:** Proporcionar uma experiência de usuário fluida para captura de imagens de placas, visualização do histórico de leituras e consulta de detalhes de veículos.
* **Funcionalidades:**
    * Câmera/Upload de Imagem: Permite ao usuário capturar ou selecionar uma imagem de placa.
    * Exibição de Resultados: Apresenta a placa reconhecida e corrigida.
    * Histórico de Leituras: Lista as placas lidas pelo usuário.
    * Consulta de Detalhes: Exibe informações detalhadas sobre um veículo específico.
* **Comunicação:** Interage com o Módulo PDI (para envio de imagens para OCR) e com o Backend Principal Java (para gerenciamento de usuários, histórico e detalhes de veículos).

## 5. Estrutura do Repositório
```
LEITOR PLACAS/
├── .idea/
├── .vscode/
├── backend/                  # Backend principal em Java/Spring Boot
│   └── (Estrutura de pastas Spring Boot: src, pom.xml, etc.)
├── frontend/                 # Aplicação frontend
│   └── (Estrutura de pastas do frontend)
├── PDI/                      # Módulo de Processamento Digital de Imagens (Python)
│   ├── pycache/
│   ├── images/               # Saídas de debug, imagens de teste
│   ├── venv/                 # Ambiente virtual Python
│   ├── requirements.txt      # Dependências Python do módulo PDI
│   └── src/
│       ├── IA/               # Inteligência Artificial / Detecção
│       │   ├── pycache/
│       │   ├── init.py
│       │   ├── bbox_cut.py   # Detecção e recorte de placa (YOLO)
│       │   └── best2.pt      # Modelo YOLO pré-treinado
│       ├── IMP/              # Image Processing / Pré-processamento
│       │   ├── pycache/
│       │   ├── init.py
│       │   └── filters.py    # Aplicação de filtros na imagem da placa
│       └── OCR/              # Optical Character Recognition / Pós-processamento
│           ├── pycache/
│           ├── init.py
│           ├── corretors.py  # Correção de erros de OCR
│           └── main.py       # API FastAPI, orquestrador do pipeline PDI
├── .gitignore
└── README.md
```

## 6. Como Rodar o Projeto

### 6.1. Pré-requisitos

* **Java Backend:**
    * JDK 21
    * Maven
    * MySQL (para desenvolvimento/produção)
* **Módulo PDI (Python):**
    * Python 3.8+
    * `pip`
* **Frontend:**
    * (Especificar pré-requisitos do framework/tecnologia usada, ex: Node.js, npm/yarn)

### 6.2. Configuração e Execução do Módulo PDI (Python)

1.  **Navegue** até o diretório `PDI`.
2.  **Crie e ative o ambiente virtual:**
    ```bash
    python3 -m venv venv
    # No Linux/macOS:
    source venv/bin/activate
    # No Windows (Prompt de Comando):
    # venv\Scripts\activate
    # No Windows (PowerShell):
    # .\venv\Scripts\Activate.ps1
    ```
3.  **Instale as dependências:**
    ```bash
    pip install -r requirements.txt
    ```
4.  **Modelo YOLO:** O arquivo `PDI/src/IA/best2.pt` é o modelo de detecção de objetos. Garanta que ele esteja presente neste caminho.
5.  **Inicie o serviço FastAPI:**
    ```bash
    uvicorn PDI.src.OCR.main:app --host 0.0.0.0 --port 8000
    ```
    *Isso iniciará o serviço PDI na porta 8000 (ou outra porta configurada).*

### 6.3. Configuração e Execução do Backend (Java)

1.  **Navegue** até o diretório `backend`.
2.  **Configurações do Banco de Dados:**
    * Configure as propriedades de conexão com o MySQL (ou H2 para testes) no arquivo `application.properties` ou `application.yml` (localizado geralmente em `backend/src/main/resources`).
    * Exemplo para MySQL:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/leitor_placas_db
        spring.datasource.username=root
        spring.datasource.password=sua_senha
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.show-sql=true
        ```
3.  **Configuração da Conexão com o Módulo PDI:**
    * O backend Java precisará saber o endereço do serviço Python (FastAPI). Isso deve ser configurado no `application.properties` ou `application.yml`, por exemplo:
        ```properties
        app.pdi.service.url=http://localhost:8000/processar/
        ```
4.  **Build do Projeto:**
    ```bash
    mvn clean install
    ```
5.  **Execute a Aplicação Spring Boot:**
    ```bash
    mvn spring-boot:run
    ```
    *A API estará disponível, geralmente na porta 8080 (ou outra porta configurada).*

### 6.4. Configuração e Execução do Frontend

1.  **Navegue** até o diretório `frontend`.
2.  **(Adicionar instruções específicas para a tecnologia do frontend aqui, ex: `npm install`, `npm start` ou `yarn install`, `yarn start`)**
    * Exemplo (para um frontend React/Vue/Angular):
        ```bash
        # Instale as dependências
        npm install # ou yarn install
        # Inicie a aplicação
        npm start   # ou yarn start
        ```
3.  **Configuração da Conexão com Backends:**
    * O frontend precisará ser configurado para se comunicar com o backend Java e o módulo PDI Python.
    * Certifique-se de que as URLs das APIs (ex: `http://localhost:8080` para o Java e `http://localhost:8000` para o PDI) estejam corretamente definidas nas variáveis de ambiente ou arquivos de configuração do frontend.

## 7. Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues para reportar bugs ou sugerir melhorias, e enviar pull requests para adicionar novas funcionalidades ou corrigir problemas.

## 8. Contribuidores

Este projeto é o resultado da colaboração e do esforço de uma equipe incrível. Agradecemos a todos os contribuidores!

| Foto | Nome | GitHub |
| :---------------------------------------------------------------------- | :----------------------- | :---------------------------------------------------------------------- |
| <img src="https://github.com/BrasileiroYan.png?size=100" width="100"> | Yan Pedro | [BrasileiroYan](https://github.com/BrasileiroYan) |
| <img src="https://github.com/otoserafim.png?size=100" width="100"> | Oto Serafim | [otoserafim](https://github.com/otoserafim) |
| <img src="https://github.com/JoaoVictorDamasceno.png?size=100" width="100"> | João Victor Damasceno | [JoaoVictorDamasceno](https://github.com/JoaoVictorDamasceno) |
| <img src="https://github.com/DerickBessa.png?size=100" width="100"> | Derick Bessa | [DerickBessa](https://github.com/DerickBessa) |


## 9. Licença

Este projeto está sob a licença [MIT License](https://opensource.org/licenses/MIT).

<p align="center">
  <img src="https://github.com/user-attachments/assets/dd43982b-4ca0-4b6a-876e-d43e4f17330a" width="250">
</p>

App que reconhece placas de veículos e retorna dados simulados
