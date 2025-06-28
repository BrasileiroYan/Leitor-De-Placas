import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';

class FirstLoginScreen extends StatelessWidget {
  final String _activateToken;
  const FirstLoginScreen(this._activateToken, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // resizeToAvoidBottomInset: false,
      body: Ink(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.indigo.shade900, AppColors.bgColor],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
        child: Center(
          child: Text(_activateToken, style: TextStyle(color: Colors.white)),
        ),
      ),
    );
  }
}
