from ultralytics import YOLO
import cv2

# 1. Carregar o modelo
model = YOLO("C:/Users/deric/Downloads/best.pt")

# 2. Ler a imagem original (com OpenCV)
img_path = "placa2.jpg"
img = cv2.imread(img_path)

# 3. Fazer a detecção
results = model(img)

# 4. Extrair a primeira predição
boxes = results[0].boxes

# 5. Verifica se há alguma detecção
if boxes is not None and len(boxes) > 0:
    # Pegamos a primeira bbox detectada
    box = boxes[0].xyxy[0].cpu().numpy().astype(int)  # formato: (x1, y1, x2, y2)

    x1, y1, x2, y2 = box

    # 6. Recorta a imagem
    cropped_plate = img[y1:y2, x1:x2]

    # 7. Redimensiona para o tamanho desejado
    resized_plate = cv2.resize(cropped_plate, (224, 64))  # ajuste o tamanho conforme necessário

    # 8. Mostra e salva
    cv2.imshow("Placa Recortada", resized_plate)
    cv2.imwrite("placa_recortada.jpg", resized_plate)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
else:
    print("Nenhuma placa detectada.")
