import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:frontend/ui/screens/confirmation_screen.dart';

Future<dynamic> showImagePreviewDialog(
  BuildContext context,
  Uint8List imageBytes, {
  bool needConfirmation = false,
}) {
  return showDialog(
    context: context,
    barrierDismissible: false,
    builder: (context) {
      return AlertDialog(
        title: const Text("Parece bom?"),
        content: ClipRect(
          child: Align(
            alignment: Alignment(-1.0, -0.21),
            widthFactor: 0.5,
            heightFactor: 0.15,
            child: Image.memory(imageBytes),
          ),
        ),
        actions: [
          IconButton.outlined(
            icon: const Icon(Icons.close),
            onPressed: () => Navigator.pop(context),
          ),
          Visibility(
            visible: needConfirmation,
            child: IconButton.outlined(
              icon: Icon(Icons.check, color: Colors.green[600]),
              onPressed: () {
                Navigator.pop(context, true);
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => ConfirmationScreen(imageBytes),
                  ),
                );
              },
            ),
          ),
        ],
      );
    },
  );
}
