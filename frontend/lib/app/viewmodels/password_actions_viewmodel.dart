import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/services/auth_service.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:get_it/get_it.dart';
import 'package:go_router/go_router.dart';

class PasswordActionsViewModel with ChangeNotifier {
  TextEditingController emailController = TextEditingController();
  TextEditingController newPasswordController = TextEditingController();
  TextEditingController confirmNewPasswordController = TextEditingController();

  bool _isLoading = false;
  bool _passwordsDontMatch = false;
  bool _passwordMatchFormat = false;

  bool get isLoading => _isLoading;
  bool get passwordsDontMatch => _passwordsDontMatch;
  bool get passwordMatchFormat => _passwordMatchFormat;

  void setLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  void setPasswordUnmatch(bool value) {
    _passwordsDontMatch = value;
    notifyListeners();
  }

  void setPasswordMatchFormat(bool value) {
    _passwordMatchFormat = value;
    notifyListeners();
  }

  Future<void> sendRecoveryEmail(BuildContext context) async {
    final authService = GetIt.instance<AuthService>();

    final username = emailController.text.trim();

    if (username.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Por favor, insira seu e-mail.')));
      return;
    }

    setLoading(true);

    try {
      final message = await authService.forgotPassword(username);
      // setLoading(false);
      if (!context.mounted) return;
      showDialog(
        context: context,
        builder:
            (context) => Dialog(
              child: Container(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  spacing: 8,
                  children: [
                    const Text(
                      "Email Enviado",
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 24),
                    ),
                    Text(message, textAlign: TextAlign.justify),
                    const SizedBox(height: 0),
                    PrimaryButton(text: "OK", onTap: () => context.go('/')),
                  ],
                ),
              ),
            ),
      );
      emailController.text = '';
    } on DioException catch (e) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('${e.response!.data['message']}')));
      context.pop();
    } finally {
      setLoading(false);
    }
  }

  Future<void> activateAccount(BuildContext context, String token) async {
    final authService = GetIt.instance<AuthService>();

    final password = newPasswordController.text.trim();
    final confirm = confirmNewPasswordController.text.trim();

    if (password.isEmpty || confirm.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Por favor, insira sua nova senha.')),
      );
      return;
    }

    // Check and state update for password format
    setPasswordMatchFormat(validateNewPassword(password));
    if (!_passwordMatchFormat) return;

    // Check and state update for password and confirm match
    setPasswordUnmatch(!(password == confirm));
    if (_passwordsDontMatch) return;

    setLoading(true);

    try {
      final message = await authService.activateAccount(token, password);
      // setLoading(false);

      if (!context.mounted) return;
      showDialog(
        context: context,
        builder:
            (context) => Dialog(
              child: Container(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  spacing: 8,
                  children: [
                    const Text(
                      "Conta Ativada!",
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 24),
                    ),
                    Text(message, textAlign: TextAlign.justify),
                    const SizedBox(height: 0),
                    PrimaryButton(text: "OK", onTap: () => context.go('/')),
                  ],
                ),
              ),
            ),
      );
    } on DioException catch (e) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('${e.response!.data['message']}')));
    } finally {
      newPasswordController.text = '';
      confirmNewPasswordController.text = '';
      setLoading(false);
    }
  }

  Future<void> resetPassword(BuildContext context, String token) async {
    final authService = GetIt.instance<AuthService>();

    final password = newPasswordController.text.trim();
    final confirm = confirmNewPasswordController.text.trim();

    if (password.isEmpty || confirm.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Por favor, insira sua nova senha.')),
      );
      return;
    }

    // Check and state update for password format
    setPasswordMatchFormat(validateNewPassword(password));
    if (!_passwordMatchFormat) return;

    // Check and state update for password and confirm match
    setPasswordUnmatch(!(password == confirm));
    if (_passwordsDontMatch) return;

    setLoading(true);

    try {
      final message = await authService.resetPassword(token, password);
      // setLoading(false);

      if (!context.mounted) return;
      showDialog(
        context: context,
        builder:
            (context) => Dialog(
              child: Container(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  spacing: 8,
                  children: [
                    const Text(
                      "Senha Redefinida!",
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 24),
                    ),
                    Text(message, textAlign: TextAlign.justify),
                    const SizedBox(height: 0),
                    PrimaryButton(text: "OK", onTap: () => context.go('/')),
                  ],
                ),
              ),
            ),
      );
    } on DioException catch (e) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('${e.response!.data['message']}')));
    } finally {
      newPasswordController.text = '';
      confirmNewPasswordController.text = '';
      setLoading(false);
    }
  }

  bool validateNewPassword(String password) {
    final regex = RegExp(
      r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&._-])[A-Za-z\d@$!%*?&._-]{8,24}$',
    );
    return regex.hasMatch(password);
  }
}
