from datetime import datetime
import re

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

        primeiras_tres_letras = ''.join(texto_corrigido[0:3])
        
        if len(texto_corrigido) > 4 and primeiras_tres_letras.isalpha() and texto_corrigido[3].isdigit():
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