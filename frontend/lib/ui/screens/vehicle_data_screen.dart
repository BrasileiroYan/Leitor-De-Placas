import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';

class PlateDataScreen extends StatelessWidget {
  final String plate;
  const PlateDataScreen(this.plate, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            Navigator.popUntil(context, (route) => route.isFirst);
          },
        ),
        backgroundColor: Colors.yellow.shade700,
        title: Text('Plate Data'),
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
              padding: const EdgeInsets.all(8.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Text(
                    plate,
                    style: TextStyle(
                      fontFamily: "GL Nummernschild",
                      fontSize: 55,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  SizedBox(
                    height: 2,
                    width: double.infinity,
                    child: Container(color: Colors.black),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
