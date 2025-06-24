from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.responses import JSONResponse
from paddleocr import PaddleOCR
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
import sys
import os

import paddleocr
print("PaddleOCR version:", paddleocr.__version__)

from src.IA.bbox_cut import recortar_placa
from src.IMP.filters import aplicar_filtros

app = FastAPI()
ocr = PaddleOCR(use_textline_orientation=True, lang='pt')


import re

def corrigir_placa_mercosul(texto_ocr: str, max_len: int = 7):
    # Remove espaços e caracteres especiais, deixa só letras maiúsculas e números
    texto_limpo = re.sub(r'[^A-Za-z0-9]', '', texto_ocr).upper()

    if len(texto_limpo) != max_len:
        # Se não tem tamanho esperado, retorna texto limpo
        return texto_limpo

    texto_corrigido = list(texto_limpo)

    letras = {'0': 'O', '1': 'I', '2': 'Z', '5': 'S', '6': 'G', '8': 'B'}
    numeros = {'O': '0', 'I': '1', 'Z': '2', 'S': '5', 'G': '6', 'B': '8'}

    for i, char in enumerate(texto_corrigido):
        if i in [0, 1, 2, 4]:  # letras
            if char in letras:
                texto_corrigido[i] = letras[char]
        else:  # números
            if char in numeros:
                texto_corrigido[i] = numeros[char]

    texto_corrigido_str = ''.join(texto_corrigido)
    print("Texto corrigido:", texto_corrigido_str)

    if len(texto_corrigido_str) > max_len:
        # Divide em duas linhas: primeiro max_len chars e o resto
        return [texto_corrigido_str[:max_len], texto_corrigido_str[max_len:]]
    else:
        return texto_corrigido_str



@app.post("/ocr/read")
async def processar_imagem(file: UploadFile = File(...)):
    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="Arquivo enviado não é uma imagem válida.")

    try:
        # 1. Lê e converte a imagem
        image_bytes = await file.read()
        image = Image.open(BytesIO(image_bytes)).convert("RGB")
        img_np = np.array(image)

        # 2. Corta a placa (bbox_cut)
        placa = recortar_placa(img_np)
    

        # 3. Aplica filtros (filters)
        placa_processada = aplicar_filtros(placa)
        placa_rgb = cv2.cvtColor(placa_processada, cv2.COLOR_GRAY2RGB)
        # 4. OCR
        resultado = ocr.predict(placa_rgb)
        linhas = resultado[0]
        print("Linhas detectadas:", linhas)
        print("Resultado OCR:", resultado)

        if not resultado or not resultado[0]:
            return {"plate": None, "mensagem": "Nada detectado"}

        texto_detectado = resultado[0]["rec_texts"][0]

        texto_corrigido = corrigir_placa_mercosul(texto_detectado)

        return {
            "plate": texto_corrigido
        }
        
    except Exception as e:
        import traceback
        print("Erro:", traceback.format_exc())
        return JSONResponse(status_code=500, content={"erro": str(e)})
