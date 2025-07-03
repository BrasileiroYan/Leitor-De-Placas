import 'package:flutter/material.dart';

Future<dynamic> showHelpDialog(BuildContext context) {
  return showDialog(
    context: context,
    barrierDismissible: true,
    builder: (context) {
      return Dialog(
        child: SizedBox(
          width: 128,
          height: 128,
          child: Center(child: Text("WIP")),
        ),
      );
    },
  );
}
