import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/password_actions_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:provider/provider.dart';

class FirstLoginScreen extends StatelessWidget {
  final String _activateToken;
  const FirstLoginScreen(this._activateToken, {super.key});

  @override
  Widget build(BuildContext context) {
    final passwordRecoveryViewModel = Provider.of<PasswordActionsViewModel>(
      context,
    );
    return Scaffold(
      extendBodyBehindAppBar: true,
      appBar: AppBar(
        foregroundColor: Colors.grey.shade300,
        actions: [
          IconButton(onPressed: () {}, icon: Icon(Icons.support_agent_rounded)),
        ],
        backgroundColor: Colors.transparent,
      ),
      backgroundColor: Colors.transparent,
      body: Ink(
        padding: EdgeInsets.symmetric(horizontal: 24),
        // padding: EdgeInsets.only(
        //   top: kToolbarHeight + MediaQuery.of(context).padding.top,
        //   left: 24,
        //   right: 24,
        //   bottom: 24,
        // ),
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.indigo.shade900, AppColors.bgColor],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
        child: Column(
          spacing: 8,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Column(
              children: [
                Text(
                  "Ativar Conta",
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Divider(
                  height: 2,
                  color: Colors.white,
                  indent: 48,
                  endIndent: 48,
                ),
              ],
            ),
            Text(
              "Digite a sua nova senha",
              style: TextStyle(color: Colors.white),
              textAlign: TextAlign.start,
            ),
            SizedBox(height: 16),
            Column(
              spacing: 8,
              children: [
                TextFormField(
                  controller: passwordRecoveryViewModel.newPasswordController,
                  cursorColor: Colors.black,
                  style: TextStyle(decorationColor: Colors.white),
                  onTapOutside: (event) => FocusScope.of(context).unfocus(),
                  decoration: InputDecoration(
                    error:
                        !passwordRecoveryViewModel.passwordMatchFormat
                            ? Text(
                              "A senha deve ter 8+ caracteres, com maiúscula, minúscula, número e caractere especial.",
                              style: TextStyle(color: Colors.red),
                            )
                            : null,
                    filled: true,
                    fillColor: Colors.white,
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(8)),
                    ),
                    hintText: "Senha",
                  ),
                ),
                TextFormField(
                  controller:
                      passwordRecoveryViewModel.confirmNewPasswordController,
                  cursorColor: Colors.black,
                  style: TextStyle(decorationColor: Colors.white),
                  onTapOutside: (event) => FocusScope.of(context).unfocus(),
                  decoration: InputDecoration(
                    errorText:
                        passwordRecoveryViewModel.passwordsDontMatch
                            ? "As senhas não correspondem"
                            : null,
                    filled: true,
                    fillColor: Colors.white,
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.all(Radius.circular(8)),
                    ),
                    hintText: "Confirmar Senha",
                  ),
                ),
              ],
            ),

            Column(spacing: 2, children: [
                
                
              ],
            ),
            PrimaryButton(
              bgColor: Colors.green,
              text: "Definir Senha",
              onTap: () async {
                await passwordRecoveryViewModel.activateAccount(
                  context,
                  _activateToken,
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
