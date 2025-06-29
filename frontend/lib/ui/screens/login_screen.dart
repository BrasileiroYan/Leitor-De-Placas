import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/login_viewmodel.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/login_field.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<LoginViewModel>(context);

    return Scaffold(
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
              child: Padding(
                padding: EdgeInsets.only(
                  left: 24,
                  top: MediaQuery.of(context).padding.top,
                  right: 24,
                ),
                child: SingleChildScrollView(
                  padding: EdgeInsets.symmetric(vertical: 24),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    spacing: 12,
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
                            controller: viewModel.emailController,
                            hintText: 'E-mail',
                          ),
                          SizedBox(height: 8),
                          LoginField(
                            controller: viewModel.passwordController,
                            obscureText: true,
                            hintText: 'Senha',
                          ),
                          Align(
                            alignment: Alignment.topRight,
                            child: GestureDetector(
                              onTap: () {
                                context.push('/passwordRecovery');
                              },
                              child: Text(
                                "Esqueci minha senha  ",
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
                          await viewModel.login(context);
                        },
                      ),
                    ],
                  ),
                ),
              ),
            ),
            viewModel.isLoading
                ? AnimatedSwitcher(
                  duration: const Duration(milliseconds: 750),
                  child: Container(
                    margin: EdgeInsets.only(
                      bottom: MediaQuery.of(context).padding.bottom,
                    ),
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.height,
                    color: Colors.black.withAlpha((255 / 2).round()),
                    child: Center(
                      child: CircularProgressIndicator(color: Colors.white),
                    ),
                  ),
                )
                : SizedBox.shrink(),
          ],
        ),
      ),
    );
  }
}
