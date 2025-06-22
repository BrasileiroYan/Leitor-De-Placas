from ultralytics import YOLO
import cv2
import matplotlib.pyplot as plt
import numpy as np
import os

def recortar_placa(img_bgr, modelo_path=None):
    # Caminho absoluto da raiz do projeto (duas pastas acima)
    raiz_projeto = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))

    # Define o caminho do modelo caso não seja passado
    if modelo_path is None:
        modelo_path = os.path.join(raiz_projeto, 'src', 'IA', 'best2.pt')
    else:
        # transforma em caminho absoluto se for relativo
        modelo_path = os.path.abspath(modelo_path)

    model = YOLO(modelo_path)

    if img_bgr is None:
        raise ValueError("Imagem inválida.")

    # Converte para RGB para o modelo/processamento


    results = model(img_bgr)
    detections = results[0]

    if len(detections.boxes) == 0:
        print("Nenhuma placa detectada.")
        return None

    # Ordena as caixas pela confiança decrescente
    boxes_sorted = sorted(detections.boxes, key=lambda b: b.conf.item(), reverse=True)
    box = boxes_sorted[0]

    x1, y1, x2, y2 = map(int, box.xyxy[0].cpu().numpy())
    conf = box.conf.item()
    cls = int(box.cls.item())
    label = results[0].names[cls]

    # Opcional: desenha bbox na imagem RGB (pode tirar depois)
    # cv2.rectangle(img_rgb, (x1, y1), (x2, y2), (0, 255, 0), 2)
    # texto = f"{label} ({conf:.2f})"
    # cv2.putText(img_rgb, texto, (x1, max(y1-10, 0)),
    #             cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2)

    # Recorta a placa na imagem original BGR
    recorte = img_bgr[y1:y2, x1:x2]

    # Salva o recorte
    cv2.imwrite("images/placa_recortada.jpg", recorte)
    print("Recorte salvo como placa_recortada.jpg")

    return recorte

if __name__ == "__main__":
    # Exemplo de uso:
    img = cv2.imread("images/placa10.jpg")
    recortar_placa(img)


