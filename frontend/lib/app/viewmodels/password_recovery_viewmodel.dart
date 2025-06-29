import 'package:flutter/widgets.dart';

class PasswordRecoveryViewModel with ChangeNotifier {
  TextEditingController emailController = TextEditingController();
  TextEditingController newPasswordController = TextEditingController();

  bool _isLoading = false;

  bool get isLoading => _isLoading;

  void setLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  Future<void> sendRecoveryEmail() async {}
}
