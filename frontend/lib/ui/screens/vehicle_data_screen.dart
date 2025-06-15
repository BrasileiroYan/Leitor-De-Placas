import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/vehicle_view_model.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/crimes_list.dart';
import 'package:frontend/ui/components/widgets/vehicle_info.dart';

class PlateDataScreen extends StatelessWidget {
  final String plate;
  final VehicleViewModel viewModel = VehicleViewModel();
  PlateDataScreen(this.plate, {super.key});

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
        title: Text('Vehicle Data'),
        centerTitle: true,
      ),
      backgroundColor: AppColors.bgColor,
      body: Container(
        margin: const EdgeInsets.all(16),
        padding: const EdgeInsets.all(8.0),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(24),
        ),
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
            Divider(height: 2, color: Colors.black),
            FutureBuilder(
              future: viewModel.getVehicleFromText(plate),
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  return Column(
                    children: [
                      VehicleInfoSection(vehicle: viewModel.vehicle),
                      const SizedBox(height: 16),
                      CrimesListSection(
                        crimes: viewModel.vehicle.owner.crimesList,
                      ),
                    ],
                  );
                } else {
                  return Center(child: CircularProgressIndicator());
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}
