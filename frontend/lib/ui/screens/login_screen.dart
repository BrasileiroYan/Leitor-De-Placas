import 'package:flutter/material.dart';
// import 'package:frontend/app/services/token_service.dart';
import 'package:frontend/app/viewmodels/login_viewmodel.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/widgets/help_widget.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/login_field.dart';
// import 'package:get_it/get_it.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final loginViewModel = LoginViewModel();

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
        child: Stack(
          children: [
            Center(
              child: SingleChildScrollView(
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 24),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    mainAxisSize: MainAxisSize.min,
                    spacing: 20,
                    children: [
                      Image.asset('assets/images/prf_icon.png', width: 128),
                      Text(
                        'SFA',
                        style: TextStyle(
                          fontFamily: 'Italiana',
                          fontSize: 80,
                          color: Colors.white,
                        ),
                      ),
                      Text(
                        "Entrar",
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 25,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      Column(
                        spacing: 4,
                        children: [
                          LoginField(
                            controller: loginViewModel.emailController,
                            hintText: 'Matrícula',
                          ),
                          SizedBox(height: 8),
                          LoginField(
                            controller: loginViewModel.passwordController,
                            obscureText: true,
                            hintText: 'Senha',
                          ),
                          Align(
                            alignment: Alignment.topRight,
                            child: GestureDetector(
                              onTap: () {
                                Navigator.pushNamed(
                                  context,
                                  "password_recovery",
                                );
                              },
                              child: Text(
                                "Recuperação de conta  ",
                                style: TextStyle(
                                  color: Colors.blue,
                                  decoration: TextDecoration.underline,
                                  decorationColor: Colors.blue,
                                  fontSize: 12,
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                      PrimaryButton(
                        text: "Entrar",
                        onTap: () async {
                          await loginViewModel.login(context);
                        },
                      ),
                      // PrimaryButton(
                      //   text: "Debug data",
                      //   onTap: () async {
                      //     String? token =
                      //         await GetIt.instance<TokenService>().getToken();
                      //     String? refreshToken =
                      //         await GetIt.instance<TokenService>()
                      //             .getRefreshToken();
                      //     debugPrint(token);
                      //     debugPrint(refreshToken);
                      //   },
                      // ),
                    ],
                  ),
                ),
              ),
            ),
            HelpWidget(lightMode: true),
          ],
        ),
      ),
    );
  }
}
