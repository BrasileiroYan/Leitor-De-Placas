import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';

Future<dynamic> showDeniedCameraPermissionDialog(BuildContext context) {
  return showDialog(
    context: context,
    barrierDismissible: false,
    builder: (context) {
      return const Dialog(child: _DeniedCameraPermissionDialog());
    },
  );
}

class _DeniedCameraPermissionDialog extends StatelessWidget {
  const _DeniedCameraPermissionDialog();

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.center,
        spacing: 8,
        children: [
          const Icon(Icons.camera_alt_outlined, color: Colors.red),
          const Text(
            "Sem permissão para acessar a câmera",
            textAlign: TextAlign.center,
            style: TextStyle(fontSize: 24),
          ),
          const Text(
            "Para continuar com a operação, o Scanneador Federal Automobilístico precisa de acesso a câmera. Para liberar, acesse as configurações do seu dispositivo.",
            textAlign: TextAlign.justify,
          ),
          const SizedBox(height: 0),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            spacing: 8,
            children: [
              TextButton(
                onPressed: () => _onNotNowClicked(context),
                child: const Text("Agora não"),
              ),
              ElevatedButton(
                onPressed: () => _onConfirmClicked(context),
                style: ElevatedButton.styleFrom(backgroundColor: Colors.orange),
                child: const Text("Abrir configurações"),
              ),
            ],
          ),
        ],
      ),
    );
  }

  _onNotNowClicked(BuildContext context) {
    Navigator.pop(context);
  }

  _onConfirmClicked(BuildContext context) {
    openAppSettings();
    Navigator.pop(context);
  }
}
