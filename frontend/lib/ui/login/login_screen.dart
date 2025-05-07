import 'package:flutter/material.dart';
import 'package:frontend/ui/_core/widgets/help_widget.dart';
import 'package:frontend/ui/home/home_screen.dart';
import 'package:frontend/ui/_core/app_colors.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.bgColor,
      body: Stack(
        children: [
          Center(
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                spacing: 20,
                children: [
                  Image.asset('assets/images/prf_icon.png', width: 76),
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
                    spacing: 16,
                    children: [
                      DecoratedBox(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.all(Radius.circular(5.0)),
                        ),
                        child: TextFormField(
                          style: TextStyle(decorationColor: Colors.white),
                          cursorColor: Colors.black,
                          decoration: InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(5.0),
                              ),
                            ),
                            hintText: 'Matrícula',
                            fillColor: Colors.white,
                          ),
                        ),
                      ),
                      DecoratedBox(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.all(Radius.circular(5.0)),
                        ),
                        child: TextFormField(
                          style: TextStyle(decorationColor: Colors.white),
                          cursorColor: Colors.black,
                          decoration: InputDecoration(
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.all(
                                Radius.circular(5.0),
                              ),
                            ),
                            hintText: 'Senha',
                            fillColor: Colors.white,
                          ),
                        ),
                      ),
                    ],
                  ),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const HomeScreen(),
                        ),
                      );
                    },
                    style: ButtonStyle(
                      foregroundColor: WidgetStatePropertyAll(Colors.white),
                      backgroundColor: WidgetStateColor.resolveWith((states) {
                        if (states.contains(WidgetState.disabled)) {
                          return Colors.grey;
                        } else if (states.contains(WidgetState.pressed)) {
                          return const Color.fromARGB(171, 255, 164, 89);
                        }
                        return Colors.green;
                      }),
                    ),
                    child: Text("Entrar", style: TextStyle(fontSize: 13)),
                  ),
                ],
              ),
            ),
          ),
          HelpWidget(lightMode: true),
        ],
      ),
    );
  }
}
