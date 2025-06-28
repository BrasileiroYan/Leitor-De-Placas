import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/login_viewmodel.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/login_field.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final loginViewModel = LoginViewModel();

  @override
  void initState() {
    loginViewModel.addListener(() {
      setState(() {});
    });
    super.initState();
  }

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
                    spacing: 16,
                    children: [
                      Image.asset('assets/images/prf_icon.png', width: 128),
                      Text(
                        'SFA',
                        style: TextStyle(
                          fontFamily: 'Italiana',
                          fontSize: 80,
                          fontWeight: FontWeight.bold,
                          color: Colors.white,
                        ),
                      ),
                      Column(
                        children: [
                          Text(
                            "Entrar",
                            style: TextStyle(
                              color: Colors.white,
                              fontSize: 25,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          Divider(
                            color: Colors.white,
                            height: 2,
                            indent: 128,
                            endIndent: 128,
                          ),
                        ],
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
            AnimatedSwitcher(
              duration: const Duration(milliseconds: 750),
              child:
                  loginViewModel.isLoading
                      ? Container(
                        width: MediaQuery.of(context).size.width,
                        height: MediaQuery.of(context).size.height,
                        color: Colors.black.withAlpha((255 / 2).round()),
                        child: Center(
                          child: CircularProgressIndicator(color: Colors.white),
                        ),
                      )
                      : null,
            ),
            // HelpWidget(lightMode: true),
          ],
        ),
      ),
    );
  }
}
