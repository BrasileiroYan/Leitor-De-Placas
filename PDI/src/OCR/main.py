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

print("PaddleOCR version:", paddleocr.__version__)

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..')))

from src.IA.bbox_cut import recortar_placa
from src.IMP.filters import aplicar_filtros

app = FastAPI()
ocr = PaddleOCR(use_textline_orientation=True, lang='pt')

contador_placas = 0  # Contador global para nomear imagens processadas

def corrigir_placa_nova(texto_ocr: str, max_len: int = 7):
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '', texto_ocr.upper())
    if len(texto_limpo) != max_len:
        return texto_limpo

    texto_corrigido = list(texto_limpo)
    letras_suspeitas = {'0': 'O', '1': 'I', '2': 'Z', '5': 'S', '6': 'G', '8': 'B'}
    numeros_suspeitos = {'O': '0', 'I': '1', 'Z': '2', 'S': '5', 'G': '6', 'B': '8'}

    for i in range(len(texto_corrigido)):
        if i in [0, 1, 2, 4]:
            texto_corrigido[i] = letras_suspeitas.get(texto_corrigido[i], texto_corrigido[i])
        else:
            texto_corrigido[i] = numeros_suspeitos.get(texto_corrigido[i], texto_corrigido[i])
    return ''.join(texto_corrigido)

def corrigir_placa_antiga(texto_ocr: str, max_len: int = 7):
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '', texto_ocr.upper())
    if len(texto_limpo) != max_len:
        return texto_limpo

    texto_corrigido = list(texto_limpo)
    letras_suspeitas = {'0': 'O', '1': 'I', '2': 'Z', '5': 'S', '6': 'G', '8': 'B'}
    numeros_suspeitos = {'O': '0', 'I': '1', 'Z': '2', 'S': '5', 'G': '6', 'B': '8'}

    for i in range(len(texto_corrigido)):
        if i in [0, 1, 2]:
            texto_corrigido[i] = letras_suspeitas.get(texto_corrigido[i], texto_corrigido[i])
        else:
            texto_corrigido[i] = numeros_suspeitos.get(texto_corrigido[i], texto_corrigido[i])
    return ''.join(texto_corrigido)

@app.post("/processar/")
async def processar_imagem(file: UploadFile = File(...)):
    global contador_placas

    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Arquivo enviado não é uma imagem válida.")

    nome_arquivo_original = file.filename

    try:
        # Salvar arquivo recebido temporariamente
        conteudo = await file.read()
        with open(nome_arquivo_original, "wb") as f:
            f.write(conteudo)

        # Abrir imagem salva
        image = Image.open(nome_arquivo_original).convert("RGB")
        img_np = np.array(image)

        # Processar a placa
        placa = recortar_placa(img_np)
        placa_processada = aplicar_filtros(placa)
        placa_rgb = cv2.cvtColor(placa_processada, cv2.COLOR_GRAY2RGB)

        resultado = ocr.predict(placa_rgb)
        if not resultado or not resultado[0]:
            # Apaga arquivo original mesmo se nada detectado
            if os.path.exists(nome_arquivo_original):
                os.remove(nome_arquivo_original)
            return PlainTextResponse("")

        texto_detectado_raw = resultado[0]["rec_texts"][0]
        texto_detectado = re.sub(r'[^A-Za-z0-9]', '', texto_detectado_raw.upper())

        nome = nome_arquivo_original.lower()
        if nome == "nova.jpg":
            texto_corrigido = corrigir_placa_nova(texto_detectado)
        elif nome == "antiga.jpg":
            texto_corrigido = corrigir_placa_antiga(texto_detectado)
        else:
            texto_corrigido = texto_detectado

        print(texto_corrigido)  # Imprime no terminal

        # Excluir arquivo original enviado
        if os.path.exists(nome_arquivo_original):
            os.remove(nome_arquivo_original)

    
        nome_placa_salvar = f"placa{contador_placas}.jpg"
        pasta_salvar = os.path.join("..", "..", "images")
        os.makedirs(pasta_salvar, exist_ok=True)
        caminho_salvar = os.path.join(pasta_salvar, nome_placa_salvar)

        cv2.imwrite(caminho_salvar, placa_processada)

        contador_placas += 1


        return PlainTextResponse(texto_corrigido)

    except Exception as e:
        import traceback
        print("Erro:", traceback.format_exc())
        # Tenta apagar arquivo original caso erro
        if os.path.exists(nome_arquivo_original):
            os.remove(nome_arquivo_original)
        return PlainTextResponse("", status_code=500)
