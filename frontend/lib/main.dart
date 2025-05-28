import 'package:flutter/material.dart';
import 'package:frontend/ui/camera/camera_screen.dart';
import 'package:frontend/ui/confirmation/confirmation_screen.dart';
import 'package:frontend/ui/home/home_screen.dart';
import 'package:frontend/ui/login/login_screen.dart';
import 'package:frontend/ui/password_recovery/password_recovery_screen.dart';
import 'package:frontend/ui/plate_data/plate_data_screen.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      routes: {
        "login": (context) => const LoginScreen(),
        "password_recovery": (context) => const PasswordRecoveryScreen(),
        "home": (context) => const HomeScreen(),
        "camera": (context) => const CameraScreen(),
        "confirmation": (context) => const ConfirmationScreen(),
        "plate_data": (context) => const PlateDataScreen(),
      },
      initialRoute: "login",
      // home: LoginScreen(),
    );
  }
}
