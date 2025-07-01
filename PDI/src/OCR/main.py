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

def corrigir_placa_nova(texto_ocr: str, max_len: int = 7):
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '', texto_ocr.upper())
    if len(texto_limpo) != max_len:
        print(f"[{datetime.now()}] Aviso: Tamanho da placa Mercosul ({len(texto_limpo)}) diferente do esperado ({max_len}). Tentando correção parcial.")
        
    texto_corrigido = list(texto_limpo)
    
    letras_para_digitos = {'O': '0', 'I': '1', 'S': '5', 'G': '6', 'B': '8', 'Z': '2', 'Q': '0'}
    digitos_para_letras = {'0': 'O', '1': 'I', '5': 'S', '6': 'G', '8': 'B', '2': 'Z'}

    letras_pos = [0, 1, 2, 4]
    numeros_pos = [3, 5, 6]

    for i in range(min(len(texto_corrigido), max_len)):
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
        print(f"[{datetime.now()}] Aviso: Tamanho da placa Antiga ({len(texto_limpo)}) diferente do esperado ({max_len}). Tentando correção parcial.")

    texto_corrigido = list(texto_limpo)
    
    letras_para_digitos = {'O': '0', 'I': '1', 'S': '5', 'G': '6', 'B': '8', 'Z': '2', 'Q': '0'}
    digitos_para_letras = {'0': 'O', '1': 'I', '5': 'S', '6': 'G', '8': 'B', '2': 'Z'}

    letras_pos = [0, 1, 2]
    numeros_pos = [3, 4, 5, 6]

    for i in range(min(len(texto_corrigido), max_len)):
        char = texto_corrigido[i]
        if i in letras_pos:
            if char.isdigit():
                texto_corrigido[i] = digitos_para_letras.get(char, char)
        elif i in numeros_pos:
            if char.isalpha():
                texto_corrigido[i] = letras_para_digitos.get(char, char)
    
    return ''.join(texto_corrigido)

def corrigir_placa_moto(texto_ocr: str, max_len: int = 7):
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '',  texto_ocr.upper())

    if len(texto_limpo) != max_len:
        print(f"[{datetime.now()}] Aviso: Tamanho da placa de MOTO ({len(texto_limpo)}) diferente do esperado ({max_len}). Tentando correção parcial.")
    
    texto_corrigido = list(texto_limpo)

    letras_para_digitos = {'O': '0', 'I': '1', 'S': '5', 'G': '6', 'B': '8', 'Z': '2', 'Q': '0'}
    digitos_para_letras = {'0': 'O', '1': 'I', '5': 'S', '6': 'G', '8': 'B', '2': 'Z'}

    if len(texto_corrigido) >= 7:
        for i in range(3):
            char = texto_corrigido[i]
            if char.isdigit():
                texto_corrigido[i] = digitos_para_letras.get(char, char)
        
        char = texto_corrigido[3]
        if char.isalpha():
            texto_corrigido[3] = letras_para_digitos.get(char, char)
        
        char = texto_corrigido[4]
        if len(texto_corrigido) > 4 and texto_corrigido[4].isalpha() and (texto_corrigido[0:3].isalpha() and texto_corrigido[3].isdigit()):
            if char.isdigit():
                texto_corrigido[4] = digitos_para_letras.get(char, char)
        else:
            if char.isalpha():
                texto_corrigido[4] = letras_para_digitos.get(char, char)

        for i in range(5, 7):
            if len(texto_corrigido) > i:
                char = texto_corrigido[i]
                if char.isalpha():
                    texto_corrigido[i] = letras_para_digitos.get(char, char)

    return ''.join(texto_corrigido)


@app.post("/processar/")
async def processar_imagem(file: UploadFile = File(...)):
    global contador_placas
    print(f"[{datetime.now()}] Início da Requisição para {file.filename}")

    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Arquivo enviado não é uma imagem válida.")

    nome_arquivo_original = file.filename
    nome_arquivo_base = os.path.splitext(nome_arquivo_original.lower())[0]
    temp_file_path = f"temp_{nome_arquivo_original}"

    try:
        conteudo = await file.read()
        with open(temp_file_path, "wb") as f:
            f.write(conteudo)

        image = Image.open(temp_file_path).convert("RGB")
        img_np = np.array(image)
        img_bgr = cv2.cvtColor(img_np, cv2.COLOR_RGB2BGR)

        placa, label = recortar_placa(img_bgr)

        if placa is None:
            if os.path.exists(temp_file_path):
                os.remove(temp_file_path)
            print(f"[{datetime.now()}] Nenhuma placa detectada para {file.filename}. Retornando 204.")
            return PlainTextResponse("", status_code=204)

        placa_processada = aplicar_filtros(placa)
        placa_rgb = cv2.cvtColor(placa_processada, cv2.COLOR_GRAY2RGB)

        resultado = ocr.predict(placa_rgb)
        
        print(f"[{datetime.now()}] Resultado RAW do OCR: {resultado}")

        main_plate_texts = [] 
        
        if resultado and isinstance(resultado, list) and len(resultado) > 0 and 'rec_texts' in resultado[0]:
            all_detected_texts = []
            if 'rec_polys' in resultado[0] and len(resultado[0]['rec_polys']) == len(resultado[0]['rec_texts']):
                for i, text in enumerate(resultado[0]['rec_texts']):
                    all_detected_texts.append({'text': text, 'y': resultado[0]['rec_polys'][i][0][1]})
            else: 
                all_detected_texts = [{'text': text, 'y': 0} for text in resultado[0]['rec_texts']]


            filtered_detections = []
            for det in all_detected_texts:
                text = det['text'].upper().strip()
                if not text:
                    continue
                if text in ['BRASIL', 'MERCOSUL', 'NACIONAL']:
                    continue
                if len(text) <= 2 and text in ['BR', 'MG', 'SP', 'RJ', 'PR', 'SC', 'RS', 'GO', 'BA', 'DF', 'AM', 'CE', 'PE', 'PA', 'PB', 'RN', 'SE', 'TO', 'MT', 'MS', 'RO', 'RR', 'AC', 'AP']:
                    continue
                filtered_detections.append(det)

            if label.lower() == "moto":
                print(f"[{datetime.now()}] Lógica de Concatenação: PLACA DE MOTO (Semântica por linhas)")
                alpha_line_candidates = []
                num_alpha_line_candidates = []

                for det in filtered_detections:
                    text_content = re.sub(r'[^A-Za-z0-9]', '', det['text'].upper())
                    if not text_content: continue

                    if len(text_content) >= 3 and text_content.isalpha() and sum(c.isalpha() for c in text_content) / len(text_content) > 0.8:
                        alpha_line_candidates.append(det)
                    elif len(text_content) >= 4 and text_content.isalnum() and sum(c.isalnum() for c in text_content) / len(text_content) > 0.8:
                        num_alpha_line_candidates.append(det)
                
                best_alpha_line = ""
                if alpha_line_candidates:
                    best_alpha_line = max(alpha_line_candidates, key=lambda x: len(x['text']))['text']

                best_num_alpha_line = ""
                if num_alpha_line_candidates:
                    best_num_alpha_line = max(num_alpha_line_candidates, key=lambda x: len(x['text']))['text']

                if best_alpha_line and best_num_alpha_line:
                    main_plate_texts = [best_alpha_line, best_num_alpha_line]
                else:
                    print(f"[{datetime.now()}] Fallback para MOTO: Ordenando por Y e tentando montar a placa.")
                    sorted_by_y = sorted(filtered_detections, key=lambda item: item['y'])
                    
                    concatenated_text = "".join([det['text'] for det in sorted_by_y]).replace('-', '').replace('.', '').replace(' ', '')
                    if len(concatenated_text) >= 7:
                        main_plate_texts = [concatenated_text[:7]]
                    else:
                        main_plate_texts = [concatenated_text]

            elif label.lower() == "old":
                print(f"[{datetime.now()}] Lógica de Concatenação: PLACA ANTIGA (Priorizando o texto mais longo)")
                if filtered_detections:
                    main_plate_texts = [max(filtered_detections, key=lambda x: len(re.sub(r'[^A-Za-z0-9]', '', x['text'])))['text']]
                else:
                    main_plate_texts = []

            elif label.lower() == "new":
                print(f"[{datetime.now()}] Lógica de Concatenação: PLACA NOVA (Priorizando o texto mais longo)")
                best_candidate = ""
                max_len_found = 0
                for det in filtered_detections:
                    cleaned_text = re.sub(r'[^A-Za-z0-9]', '', det['text'])
                    if len(cleaned_text) == 7:
                        best_candidate = det['text']
                        break
                    if len(cleaned_text) > max_len_found:
                        max_len_found = len(cleaned_text)
                        best_candidate = det['text']
                
                if best_candidate:
                    main_plate_texts = [best_candidate]
                else:
                    main_plate_texts = []


            else:
                print(f"[{datetime.now()}] Lógica de Concatenação: Tipo de Placa Desconhecido (Padrão: concatena tudo após filtragem)")
                sorted_by_y = sorted(filtered_detections, key=lambda item: item['y'])
                main_plate_texts = [det['text'] for det in sorted_by_y]
        
        texto_placa_completa_raw = "".join(main_plate_texts)

        if not texto_placa_completa_raw:
            if os.path.exists(temp_file_path):
                os.remove(temp_file_path)
            print(f"[{datetime.now()}] Texto da placa RAW vazio para {file.filename}. Retornando 204.")
            return PlainTextResponse("", status_code=204)

        texto_detectado = re.sub(r'[^A-Za-z0-9]', '', texto_placa_completa_raw.upper())

        texto_corrigido = ""
        if label.lower() == "moto":
            print(f"[{datetime.now()}] Aplicando correção para: PLACA DE MOTO")
            texto_corrigido = corrigir_placa_moto(texto_detectado)
        elif label.lower() == "new":
            print(f"[{datetime.now()}] Aplicando correção para: PLACA NOVA")
            texto_corrigido = corrigir_placa_nova(texto_detectado)
        elif label.lower() == "old":
            print(f"[{datetime.now()}] Aplicando correção para: PLACA ANTIGA")
            texto_corrigido = corrigir_placa_antiga(texto_detectado)
        else:
            print(f"[{datetime.now()}] Aplicando correção para: Tipo de Placa Desconhecido (Padrão para Nova)")
            texto_corrigido = corrigir_placa_nova(texto_detectado)
            
        if len(texto_corrigido) < 7:
            texto_corrigido = texto_corrigido.ljust(7, 'X')
        elif len(texto_corrigido) > 7:
            texto_corrigido = texto_corrigido[:7]
            
        print(f"Placa detectada (RAW): '{texto_placa_completa_raw}'")
        print(f"Placa corrigida: '{texto_corrigido}'")

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