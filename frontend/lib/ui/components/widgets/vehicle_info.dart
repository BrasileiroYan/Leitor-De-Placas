import 'package:flutter/material.dart';
import 'package:frontend/app/models/vehicle.dart';

class VehicleInfoSection extends StatelessWidget {
  final Vehicle vehicle;

  const VehicleInfoSection({super.key, required this.vehicle});

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SectionTitle('Vehicle Info'),
        InfoText('Type: ${vehicle.vehicleType}'),
        InfoText('Brand: ${vehicle.brand}'),
        InfoText('Model: ${vehicle.model}'),
        InfoText('Color: ${vehicle.color}'),
        InfoText('Year: ${vehicle.fabricationYear}'),
        InfoText('IPVA Status: ${vehicle.ipvaStatus}'),
        const SizedBox(height: 8),
        SectionTitle('Owner Info'),
        InfoText('Name: ${vehicle.owner.fullName}'),
        InfoText('RG: ${vehicle.owner.rg}'),
        InfoText('CPF: ${vehicle.owner.cpf}'),
        InfoText('Birth Date: ${vehicle.owner.birthDate}'),
        InfoText('Gender: ${vehicle.owner.genero}'),
        InfoText('License Category: ${vehicle.owner.licenseCategory}'),
      ],
    );
  }
}

Widget SectionTitle(String text) {
  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 8),
    child: Text(
      text,
      style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
    ),
  );
}

Widget InfoText(String text) {
  return Padding(
    padding: const EdgeInsets.symmetric(vertical: 2),
    child: Text(text, style: const TextStyle(fontSize: 16)),
  );
}
