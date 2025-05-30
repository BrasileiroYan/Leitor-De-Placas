import cv2
import pytesseract

pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"


# 1. Leia a imagem
imagem = cv2.imread("placa.jpg")

# 2. Converta para escala de cinza
cinza = cv2.cvtColor(imagem, cv2.COLOR_BGR2GRAY)

# 3. Reduza ruídos
blur = cv2.GaussianBlur(cinza, (5, 5), 0)
clahe = cv2.createCLAHE(clipLimit=5.0, tileGridSize=(8, 8))
imagem_clahe = clahe.apply(blur)


# 4. Binarize a imagem com Otsu
_, binarizada = cv2.threshold(imagem_clahe, 0, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)




# 5. Aumente a imagem para facilitar leitura (escala 2x)
ampliada = cv2.resize(binarizada, None, fx=4, fy=4 , interpolation=cv2.INTER_LINEAR)



# 6. Configuração do Tesseract
custom_config = r'--oem 3 --psm 7 -c tessedit_char_whitelist=ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'

# 7. Extraia o texto
texto = pytesseract.image_to_string(ampliada, config=custom_config)


import re 
# 8. Mostre o resultado

texto = texto.strip()

texto_limpo = re.sub(r'[^A-Z0-9]', '', texto)
placa_mercosul = texto_limpo[:7]  # Considerando os 7 primeiros caracteres
print("Placa Mercosul:", placa_mercosul)


# (opcional) Mostrar imagem final usada
cv2.imshow("Imagem tratada", ampliada)
cv2.waitKey(0)
cv2.destroyAllWindows()