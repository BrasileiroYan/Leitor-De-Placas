import 'package:flutter/material.dart';
import 'package:frontend/app/helpers/plate_formater.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/_core/app_background_gradient.dart';
import 'package:frontend/ui/components/widgets/crimes_list.dart';
import 'package:frontend/ui/components/widgets/vehicle_info.dart';
import 'package:go_router/go_router.dart';

class VehicleDataScreen extends StatelessWidget {
  final Vehicle _vehicle;
  const VehicleDataScreen(this._vehicle, {super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
          icon: Icon(Icons.arrow_back),
          onPressed: () {
            context.go('/home');
          },
        ),
        backgroundColor: AppColors.amber,
      ),
      body: AppBackgroundGradient(
        child: Container(
          margin: const EdgeInsets.all(16),
          padding: const EdgeInsets.all(8.0),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(24),
          ),
          child: Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text(
                  PlateFormater.formatPlate(
                    _vehicle.plate,
                    dotMode: true,
                    withSpacing: true,
                  ),
                  style: TextStyle(
                    fontFamily: "GL Nummernschild",
                    fontSize: 55,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Divider(thickness: 2, height: 2, color: Colors.black),
                Column(
                  children: [
                    VehicleInfoSection(vehicle: _vehicle),
                    const SizedBox(height: 16),
                    CrimesListSection(crimes: _vehicle.owner.crimesList),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
