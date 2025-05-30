import 'package:flutter/material.dart';
import 'package:frontend/ui/screens/confirmation_screen.dart';
import 'package:frontend/ui/screens/home_screen.dart';
import 'package:frontend/ui/screens/login_screen.dart';
import 'package:frontend/ui/screens/password_recovery_screen.dart';
import 'package:frontend/ui/screens/plate_data_screen.dart';

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "SFA",
      routes: {
        "login": (context) => const LoginScreen(),
        "password_recovery": (context) => const PasswordRecoveryScreen(),
        "home": (context) => const HomeScreen(),
        "confirmation": (context) => const ConfirmationScreen(),
        "plate_data": (context) => const PlateDataScreen(),
      },
      initialRoute: "login",
      // home: LoginScreen(),
    );
  }
}
