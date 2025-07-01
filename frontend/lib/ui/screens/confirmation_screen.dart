import 'dart:typed_data';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/help_widget.dart';
import 'package:frontend/ui/screens/vehicle_data_screen.dart';

class ConfirmationScreen extends StatelessWidget {
  // final Uint8List imageBytes;
  final String plate;
  final XFile file;
  const ConfirmationScreen(this.file, this.plate, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.yellow.shade700,
        title: Text('Plate Confirmation Screen'),
        centerTitle: true,
      ),
      backgroundColor: AppColors.bgColor,
      body: Container(
        margin: const EdgeInsets.all(16),
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
                  SizedBox(height: 1, width: double.infinity),
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
                        child: FutureBuilder<Uint8List>(
                          future: file.readAsBytes(),
                          builder: (context, snapshot) {
                            if (snapshot.hasData) {
                              return Image.memory(snapshot.data!, height: 256);
                            } else {
                              return CircularProgressIndicator();
                            }
                          },
                        ),
                      ), //Meant to receive the picture taken in CameraScreen
                    ],
                  ),
                  SizedBox(height: 32),
                  Column(
                    children: [
                      Text(
                        plate, //Meant to receive the plate number as string from backend
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
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) {
                                  return VehicleDataScreen(
                                    Vehicle.getExample(),
                                  );
                                },
                              ),
                            );
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
    );
  }
}
