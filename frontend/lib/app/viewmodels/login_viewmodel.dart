import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/services/auth_service.dart';
import 'package:get_it/get_it.dart';
import 'package:go_router/go_router.dart';

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
        SnackBar(content: Text('Por favor, insira seu usuário e senha.')),
      );
      return;
    }

    setLoading(true);

    try {
      final success = await authService.login(email, password);

      if (success) {
        if (!context.mounted) return;
        context.go('/home');
      } else {
        if (!context.mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Login Falhou! Tente novamente.')),
        );
      }
    } on DioException {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('E-mail ou senha incorretos. Tente novamente.')),
      );
    } finally {
      passwordController.text = '';
      setLoading(false);
    }
  }
}
