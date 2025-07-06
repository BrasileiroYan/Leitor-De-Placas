import 'dart:typed_data';

import 'package:flutter/material.dart';
// import 'package:frontend/ui/screens/confirmation_screen.dart';

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
        content: ClipRect(child: Image.memory(imageBytes, height: 256)),
        actions: [
          IconButton.outlined(
            icon: const Icon(Icons.close),
            onPressed: () => Navigator.pop(context, false),
          ),
          Visibility(
            visible: needConfirmation,
            child: IconButton.outlined(
              icon: Icon(Icons.check, color: Colors.green[600]),
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
