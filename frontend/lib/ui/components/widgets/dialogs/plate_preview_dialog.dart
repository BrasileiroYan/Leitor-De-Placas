import 'package:flutter/material.dart';
import 'package:frontend/app/helpers/plate_formater.dart';

Future<dynamic> showPlatePreviewDialog(
  BuildContext context,
  String plate, {
  bool needConfirmation = false,
}) {
  return showDialog(
    context: context,
    barrierDismissible: false,
    builder: (context) {
      return AlertDialog(
        title: const Text("Confirme a placa:"),
        content: Text(
          PlateFormater.formatPlate(plate),
          style: TextStyle(
            fontFamily: "GL Nummernschild",
            fontSize: 40,
            fontWeight: FontWeight.bold,
          ),
          textAlign: TextAlign.center,
        ),
        actions: [
          IconButton.outlined(
            icon: const Icon(Icons.close),
            onPressed: () => Navigator.pop(context, false),
          ),
          Visibility(
            visible: needConfirmation,
            child: IconButton.outlined(
              icon: Icon(Icons.check, color: Colors.green),
              onPressed: () {
                Navigator.pop(context, true);
              },
            ),
          ),
        ],
      );
    },
  );
}
