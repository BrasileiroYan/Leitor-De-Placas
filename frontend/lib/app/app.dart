import 'package:flutter/material.dart';
import 'package:frontend/ui/screens/home_screen.dart';
import 'package:frontend/ui/screens/login_screen.dart';
import 'package:frontend/ui/screens/password_recovery_screen.dart';

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "SFA",
      routes: {
        "login": (context) => const LoginScreen(),
        "password_recovery": (context) => const PasswordRecoveryScreen(),
        "home": (context) => HomeScreen(),
      },
      initialRoute: "login",
      // home: LoginScreen(),
    );
  }
}
