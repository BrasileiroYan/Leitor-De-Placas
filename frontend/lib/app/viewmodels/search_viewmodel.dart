import 'package:flutter/material.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/app/services/vehicle_service.dart';
import 'package:frontend/ui/screens/vehicle_data_screen.dart';
import 'package:get_it/get_it.dart';

class SearchViewModel with ChangeNotifier {
  final TextEditingController searchController = TextEditingController();

  bool _isLoading = false;

  bool get isLoading => _isLoading;

  void setLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  Future<void> searchPlateFromText(BuildContext context) async {
    final vehicleService = GetIt.instance<VehicleService>();

    final plate = searchController.text.trim();

    if (plate.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Por favor, digite uma placa')));
      return;
    }

    // Verifica se a placa tem formato válido
    final regex1 = RegExp(r'^[A-Z]{3}-\d{4}$'); // Format: ABC-1234
    final regex2 = RegExp(r'^[A-Z]{3}\d[A-Z]\d{2}$'); // Format: ABC1C23

    final isValidPlate = regex1.hasMatch(plate) || regex2.hasMatch(plate);

    Vehicle? vehicle;

    if (!isValidPlate) {
      if (!context.mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Formato inválido, tente novamente!')),
      );
    }
    setLoading(true);

    vehicle = await vehicleService.getVehicleFromPlate(plate);

    setLoading(false);

    if (vehicle != null) {
      if (!context.mounted) return;
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) {
            return VehicleDataScreen(vehicle!);
          },
        ),
      );
    } else {
      // showPlateNotFoundDialog(plate);
    }
  }

  Future<void> searchPlateFromImage(BuildContext context) async {}
}
