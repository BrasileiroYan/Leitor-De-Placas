import os
from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.responses import PlainTextResponse
from paddleocr import PaddleOCR
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
import sys
import re
import paddleocr
from datetime import datetime


print("PaddleOCR version:", paddleocr.__version__)

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from src.IA.bbox_cut import recortar_placa
from src.IMP.filters import aplicar_filtros

app = FastAPI()
ocr = PaddleOCR(use_textline_orientation=True, lang='pt')

contador_placas = 0 

# --- Funções de Correção de Placa ---

def corrigir_placa_nova(texto_ocr: str, max_len: int = 7):
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '', texto_ocr.upper())
    if len(texto_limpo) != max_len:
        return texto_limpo

    texto_corrigido = list(texto_limpo)
    
    letras_para_digitos = {'O': '0', 'I': '1', 'S': '5', 'G': '6', 'B': '8', 'Z': '2'}
    digitos_para_letras = {'0': 'O', '1': 'I', '5': 'S', '6': 'G', '8': 'B', '2': 'Z'}

    letras_pos = [0, 1, 2, 4]
    numeros_pos = [3, 5, 6]
    
    for i in range(max_len):
        char = texto_corrigido[i]
        if i in letras_pos:
            if char.isdigit(): 
                texto_corrigido[i] = digitos_para_letras.get(char, char)
        elif i in numeros_pos:
            if char.isalpha(): 
                texto_corrigido[i] = letras_para_digitos.get(char, char)
    
    return ''.join(texto_corrigido)

def corrigir_placa_antiga(texto_ocr: str, max_len: int = 7):
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '', texto_ocr.upper())
    if len(texto_limpo) != max_len:
        return texto_limpo

    texto_corrigido = list(texto_limpo)
    
    letras_para_digitos = {'O': '0', 'I': '1', 'S': '5', 'G': '6', 'B': '8', 'Z': '2'}
    digitos_para_letras = {'0': 'O', '1': 'I', '5': 'S', '6': 'G', '8': 'B', '2': 'Z'}

    letras_pos = [0, 1, 2]
    numeros_pos = [3, 4, 5, 6]

    for i in range(max_len):
        char = texto_corrigido[i]
        if i in letras_pos:
            if char.isdigit():
                texto_corrigido[i] = digitos_para_letras.get(char, char)
        elif i in numeros_pos:
            if char.isalpha():
                texto_corrigido[i] = letras_para_digitos.get(char, char)
    
    return ''.join(texto_corrigido)

def corrigir_placa_moto(texto_ocr: str, max_len: int = 7):
    return corrigir_placa_antiga(texto_ocr, max_len)


# --- Endpoint de Processamento de Imagem ---
@app.post("/processar/")
async def processar_imagem(file: UploadFile = File(...)):
    global contador_placas
    print(f"[{datetime.now()}] Início da Requisição para {file.filename}")

    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Arquivo enviado não é uma imagem válida.")

    nome_arquivo_original = file.filename
    nome_arquivo_base = os.path.splitext(nome_arquivo_original.lower())[0] # Ex: "nova", "antiga", "moto"
    temp_file_path = f"temp_{nome_arquivo_original}"

    try:
        conteudo = await file.read()
        with open(temp_file_path, "wb") as f:
            f.write(conteudo)

        image = Image.open(temp_file_path).convert("RGB")
        img_np = np.array(image)
        img_bgr = cv2.cvtColor(img_np, cv2.COLOR_RGB2BGR)

        placa = recortar_placa(img_bgr)
        
        if placa is None:
            if os.path.exists(temp_file_path):
                os.remove(temp_file_path)
            return PlainTextResponse("", status_code=204)

        placa_processada = aplicar_filtros(placa)
        placa_rgb = cv2.cvtColor(placa_processada, cv2.COLOR_GRAY2RGB)

        resultado = ocr.predict(placa_rgb)
        
        print(f"[{datetime.now()}] Resultado RAW do OCR: {resultado}")

        # --- LÓGICA DE COLETA E CONCATENAÇÃO DOS TEXTOS DO OCR (AGORA BASEADA NO NOME DO ARQUIVO) ---
        main_plate_texts = [] 
        
        if resultado and isinstance(resultado, list) and len(resultado) > 0 and 'rec_texts' in resultado[0]:
            all_detected_texts = []
            if 'rec_polys' in resultado[0] and len(resultado[0]['rec_polys']) == len(resultado[0]['rec_texts']):
                for i, text in enumerate(resultado[0]['rec_texts']):
                    all_detected_texts.append({'text': text, 'y': resultado[0]['rec_polys'][i][0][1]})
            else: 
                all_detected_texts = [{'text': text, 'y': 0} for text in resultado[0]['rec_texts']]


            # 1. Pré-filtragem de ruído comum (BRASIL, MERCOSUL, etc.)
            filtered_detections = []
            for det in all_detected_texts:
                text = det['text'].upper()
                if text in ['BRASIL', 'MERCOSUL', 'NACIONAL']:
                    continue
                # Siglas de estado para filtrar (baseado em que não fazem parte do número principal)
                # Esta heurística é mais segura se o YOLO recortar bem a placa principal e deixar o "BR" de fora
                if len(text) <= 2 and text in ['BR', 'MG', 'SP', 'RJ', 'PR', 'SC', 'RS', 'GO', 'BA', 'DF', 'AM', 'CE', 'PE', 'PA', 'PB', 'RN', 'SE', 'TO', 'MT', 'MS', 'RO', 'RR', 'AC', 'AP']:
                    continue
                filtered_detections.append(det)

            # 2. Lógica de Ordenação Específica para MOTO
            if nome_arquivo_base == "moto":
                print(f"[{datetime.now()}] Lógica de Ordenação: PLACA DE MOTO (Filtrada e Semântica)")
                alpha_line = ""
                num_alpha_line = ""

                # Encontra a linha alfabética e a linha numérica/alfanumérica
                for det in filtered_detections:
                    if det['text'].isalpha() and len(det['text']) >= 3: # Ex: BRA, DHM (3 letras ou mais)
                        alpha_line = det['text']
                    elif det['text'].isalnum() and (det['text'].isdigit() or any(c.isdigit() for c in det['text'])): # Ex: 49CC, 6197
                        num_alpha_line = det['text']
                
                if alpha_line and num_alpha_line:
                    main_plate_texts = [alpha_line, num_alpha_line]
                else: # Fallback para casos não identificados por tipo (ordena por Y)
                    sorted_by_y = sorted(filtered_detections, key=lambda item: item['y'])
                    main_plate_texts = [det['text'] for det in sorted_by_y]
            
            else: # Lógica de Ordenação Padrão para CARROS (nova/antiga)
                print(f"[{datetime.now()}] Lógica de Ordenação: PLACA DE CARRO (Padrão por Y)")
                sorted_by_y = sorted(filtered_detections, key=lambda item: item['y'])
                main_plate_texts = [det['text'] for det in sorted_by_y]
        
        texto_placa_completa_raw = "".join(main_plate_texts)
        # --- FIM DA LÓGICA DE COLETA E CONCATENAÇÃO ---

        if not texto_placa_completa_raw:
            if os.path.exists(temp_file_path):
                os.remove(temp_file_path)
            return PlainTextResponse("", status_code=204)

        texto_detectado = re.sub(r'[^A-Za-z0-9]', '', texto_placa_completa_raw.upper())

        # --- Lógica para escolher a função de correção (AGORA TOTALMENTE BASEADA NO NOME DO ARQUIVO) ---
        if nome_arquivo_base == "moto":
            print(f"[{datetime.now()}] Aplicando correção para: PLACA DE MOTO")
            texto_corrigido = corrigir_placa_moto(texto_detectado)
        elif nome_arquivo_base == "nova":
            print(f"[{datetime.now()}] Aplicando correção para: PLACA NOVA")
            texto_corrigido = corrigir_placa_nova(texto_detectado)
        elif nome_arquivo_base == "antiga":
            print(f"[{datetime.now()}] Aplicando correção para: PLACA ANTIGA")
            texto_corrigido = corrigir_placa_antiga(texto_detectado)
        else: # Fallback caso o nome não seja nenhum dos esperados, usa nova
            print(f"[{datetime.now()}] Aplicando correção para: Tipo de Placa Desconhecido (Padrão para Nova)")
            texto_corrigido = corrigir_placa_nova(texto_detectado)
            
        print(f"Placa detectada (RAW): {texto_placa_completa_raw}")
        print(f"Placa corrigida: {texto_corrigido}")

        nome_placa_salvar = f"placa{contador_placas}.jpg"
        pasta_salvar = os.path.join("..", "..", "images")
        os.makedirs(pasta_salvar, exist_ok=True)
        caminho_salvar = os.path.join(pasta_salvar, nome_placa_salvar)
        cv2.imwrite(caminho_salvar, placa_processada)

        contador_placas += 1

        if os.path.exists(temp_file_path):
            os.remove(temp_file_path)
        
        return PlainTextResponse(texto_corrigido)

    except Exception as e:
        import traceback
        print("Erro:", traceback.format_exc())
        if os.path.exists(temp_file_path):
            os.remove(temp_file_path)
        raise HTTPException(status_code=500, detail="Erro interno ao processar a imagem.")