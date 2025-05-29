import 'package:flutter/material.dart';

import 'package:frontend/ui/components/_core/app_colors.dart';

class PasswordRecoveryScreen extends StatelessWidget {
  const PasswordRecoveryScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.yellow.shade700,
        title: Text('Password Recovery'),
        centerTitle: true,
      ),
      backgroundColor: AppColors.bgColor,
    );
  }
}
