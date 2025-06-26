import 'package:flutter/material.dart';
import 'package:frontend/app/services/auth_service.dart';
import 'package:get_it/get_it.dart';

class LoginViewModel extends ChangeNotifier {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  bool _isLoading = false;

  bool get isLoading => _isLoading;

  void setLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  Future<void> login(BuildContext context) async {
    final authService = GetIt.instance<AuthService>();

    final email = emailController.text.trim();
    final password = passwordController.text.trim();

    if (email.isEmpty || password.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Por favor, preencha todos os campos.')),
      );
      return;
    }

    setLoading(true);

    final success = await authService.login(email, password);
    setLoading(false);

    if (success) {
      if (!context.mounted) return;
      Navigator.pushReplacementNamed(context, "home");
    } else {
      if (!context.mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Login Falhou! Tente novamente.')));
    }
  }
}
