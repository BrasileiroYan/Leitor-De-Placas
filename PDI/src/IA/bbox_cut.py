from ultralytics import YOLO
import cv2
import matplotlib.pyplot as plt
import numpy as np
import os

def recortar_placa(img_bgr, modelo_path=None):
    raiz_projeto = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', '..'))


    if modelo_path is None:
        modelo_path = os.path.join(raiz_projeto, 'src', 'IA', 'best2.pt')
    else:
        modelo_path = os.path.abspath(modelo_path)

    model = YOLO(modelo_path)

    if img_bgr is None:
        raise ValueError("Imagem inválida.")


    results = model(img_bgr, verbose=False)
    detections = results[0]

    if len(detections.boxes) == 0:
        cv2.imwrite("images/placastate.jpg", img_bgr)
        print("Nenhuma placa detectada.")
        return None
    

    boxes_sorted = sorted(detections.boxes, key=lambda b: b.conf.item(), reverse=True)
    box = boxes_sorted[0]

    x1, y1, x2, y2 = map(int, box.xyxy[0].cpu().numpy())
    conf = box.conf.item()
    cls = int(box.cls.item())
    label = results[0].names[cls]

    img_debug = img_bgr.copy()

    overlay = np.zeros_like(img_debug, dtype=np.uint8)

    mask_color = (0, 255, 0)
    cv2.rectangle(overlay, (x1, y1), (x2, y2), mask_color, -1)

    alpha = 0.4

    img_debug = cv2.addWeighted(overlay, alpha, img_debug, 1 - alpha, 0)

    texto = f"{label} ({conf:.2f})"
    cv2.putText(img_debug, texto, (x1, max(y1-10, 0)),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 0, 0), 2)

    cv2.imwrite("images/debug_placa_mask.jpg", img_debug)
    print("Imagem de debug com máscara salva como debug_placa_mask.jpg")

    recorte = img_bgr[y1:y2, x1:x2]

    cv2.imwrite("images/placa_recortada.jpg", recorte)
    print("Recorte salvo como placa_recortada.jpg")
    print(recorte.shape)

    menor = min (recorte.shape[0], recorte.shape[1])
    maior = max (recorte.shape[0], recorte.shape[1])

    if (maior - menor) / maior <= 0.22:
        

        print("A placa é quadrada.")
        label = "moto"
    else:
        print("A placa e retangular.")

    return recorte,  label

if __name__ == "__main__":
    img = cv2.imread("images/placa10.jpg")
    if img is not None:
        recortar_placa(img)
    else:
        print("Erro: Não foi possível carregar a imagem. Verifique o caminho.")