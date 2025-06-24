import cv2
import numpy as np
import matplotlib.pyplot as plt

def aplicar_filtros(img):
    # Seu código original:
    if img is None:
        raise ValueError("Imagem não encontrada no caminho informado.")

    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    blur = cv2.GaussianBlur(gray, (1, 1), 3)
    eroded = cv2.erode(blur, np.ones((5, 3), np.uint8), iterations=1)
    dilated = cv2.dilate(eroded, np.ones((3, 1), np.uint8), iterations=1)
    binarized = cv2.adaptiveThreshold(
        dilated, 255,
        cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
        cv2.THRESH_BINARY_INV,
        81, 40)

    cv2.imwrite("images/placa_pos_processada.jpg", binarized)
    #plt.imshow(binarized, cmap='gray')
    #plt.title("Binarized Image")
    #plt.axis('off')
    #plt.show()
    return binarized

if __name__ == "__main__":
    aplicar_filtros("placa_recortada.jpg")  # o parâmetro não é usado no seu código, pode passar None
