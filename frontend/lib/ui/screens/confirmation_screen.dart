import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/help_widget.dart';

class ConfirmationScreen extends StatelessWidget {
  final Uint8List imageBytes;
  const ConfirmationScreen(this.imageBytes, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.yellow.shade700,
        title: Text('Plate Confirmation Screen'),
        centerTitle: true,
      ),
      backgroundColor: AppColors.bgColor,
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Stack(
            children: [
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(height: 32, width: double.infinity),
                    Column(
                      spacing: 8,
                      children: [
                        Text(
                          "Confirme a Placa",
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        ClipRRect(
                          borderRadius: BorderRadius.circular(16),
                          child: Align(
                            alignment: Alignment(0, -0.21),
                            widthFactor: 0.9,
                            heightFactor: 0.15,
                            child: Image.memory(imageBytes),
                          ),
                        ), //Meant to receive the picture taken in CameraScreen
                      ],
                    ),
                    SizedBox(height: 32),
                    Column(
                      children: [
                        Text(
                          "ABC-1234", //Meant to receive the plate number as string from backend
                          style: TextStyle(
                            fontFamily: "GL Nummernschild",
                            fontSize: 40,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        SizedBox(
                          height: 2,
                          width: 128,
                          child: Container(color: Colors.black),
                        ),
                      ],
                    ),
                    SizedBox(height: 32),
                    Column(
                      spacing: 8,
                      children: [
                        SizedBox(
                          width: 192,
                          child: ElevatedButton(
                            onPressed: () {
                              Navigator.pushNamed(context, "plate_data");
                            },
                            style: ButtonStyle(
                              foregroundColor: WidgetStatePropertyAll(
                                Colors.white,
                              ),
                              backgroundColor: WidgetStatePropertyAll(
                                Colors.green,
                              ),
                              shape: WidgetStatePropertyAll(
                                RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(6),
                                ),
                              ),
                            ),

                            child: Text(
                              "Confirmar",
                              style: TextStyle(fontSize: 13),
                            ),
                          ),
                        ),
                        SizedBox(
                          width: 192,
                          child: ElevatedButton(
                            onPressed: () {
                              Navigator.pop(context);
                            },
                            style: ButtonStyle(
                              foregroundColor: WidgetStatePropertyAll(
                                Colors.white,
                              ),
                              backgroundColor: WidgetStatePropertyAll(
                                Colors.red.shade900,
                              ),
                              shape: WidgetStatePropertyAll(
                                RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(6),
                                ),
                              ),
                            ),

                            child: Text(
                              "Tentar Novamente",
                              style: TextStyle(fontSize: 13),
                            ),
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: 32),
                    Column(
                      spacing: 4,
                      children: [
                        Text("Não está conseguindo?"),
                        SizedBox(
                          width: 192,
                          child: ElevatedButton(
                            onPressed: () {
                              Navigator.popUntil(
                                context,
                                (route) => route.isFirst,
                              );
                            },
                            style: ButtonStyle(
                              foregroundColor: WidgetStatePropertyAll(
                                Colors.white,
                              ),
                              backgroundColor: WidgetStatePropertyAll(
                                Colors.blue.shade900,
                              ),
                              shape: WidgetStatePropertyAll(
                                RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(6),
                                ),
                              ),
                            ),

                            child: Text(
                              "Digitar Placa",
                              style: TextStyle(fontSize: 13),
                            ),
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: 32),
                  ],
                ),
              ),
              HelpWidget(),
            ],
          ),
        ),
      ),
    );
  }
}
