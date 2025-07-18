import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/password_actions_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/_core/app_background_gradient.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/widgets/dialogs/help_dialog.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

class PasswordRecoveryScreen extends StatelessWidget {
  const PasswordRecoveryScreen({super.key});

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
          IconButton(
            onPressed: () {
              context.push('/support', extra: false);
            },
            icon: Icon(Icons.support_agent_rounded),
          ),
        ],
        backgroundColor: Colors.transparent,
      ),
      backgroundColor: Colors.transparent,
      body: AppBackgroundGradient(
        padding: EdgeInsets.symmetric(horizontal: 24),
        child: Center(
          child: Column(
            spacing: 8,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Column(
                children: [
                  Text(
                    "Recuperar Senha",
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
                "Digite seu e-mail para realizar a recuperação",
                style: TextStyle(color: Colors.white),
                textAlign: TextAlign.center,
              ),
              SizedBox(height: 16),
              Column(
                spacing: 2,
                children: [
                  TextFormField(
                    controller: passwordRecoveryViewModel.emailController,
                    cursorColor: Colors.black,
                    style: TextStyle(decorationColor: Colors.white),
                    onTapOutside: (event) => FocusScope.of(context).unfocus(),
                    decoration: InputDecoration(
                      filled: true,
                      fillColor: Colors.white,
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.all(Radius.circular(8)),
                      ),
                      hintText: "E-mail",
                    ),
                  ),
                  Align(
                    alignment: Alignment.topRight,
                    child: GestureDetector(
                      onTap: () {
                        showHelpDialog(context);
                      },
                      child: Text(
                        "Primeiro acesso?  ",
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
                bgColor: AppColors.amber,
                text: "Solicitar recuperação",
                onTap: () async {
                  await passwordRecoveryViewModel.sendRecoveryEmail(context);
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
