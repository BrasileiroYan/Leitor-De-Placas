import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/helpers/plate_formater.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/app/services/plate_service.dart';
import 'package:frontend/app/services/vehicle_service.dart';
import 'package:frontend/ui/components/widgets/dialogs/plate_preview_dialog.dart';
import 'package:get_it/get_it.dart';
import 'package:go_router/go_router.dart';

abstract class SearchViewModel with ChangeNotifier {
  final TextEditingController searchController = TextEditingController(
    text: ' ',
  );
  Set<String>? searchScope;

  bool _isLoading = false;
  bool _isSearching = false;

  bool get isLoading => _isLoading;
  bool get isSearching => _isSearching;

  void setLoading(bool value) {
    _isLoading = value;
    notifyListeners();
  }

  void setSearching(bool value) {
    _isSearching = value;
    notifyListeners();
  }

  Future<Set<String>?> fetchSearchScope();
}

class PlateSearchViewModel extends SearchViewModel {
  Future<void> searchPlateFromText(BuildContext context) async {
    final vehicleService = GetIt.instance<VehicleService>();

    String plate = searchController.text.trim();

    if (plate.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Por favor, digite uma placa')));
      return;
    }

    // Verifica se a placa tem formato válido
    final regex1 = RegExp(r'^[A-Z]{3}-\d{4}$'); // Format: ABC-1234
    final regex2 = RegExp(r'^[A-Z]{3}\d{4}$'); // Format: ABC1234
    final regex3 = RegExp(r'^[A-Z]{3}\d[A-Z]\d{2}$'); // Format: ABC1C23

    final isValidPlate =
        regex1.hasMatch(plate) ||
        regex2.hasMatch(plate) ||
        regex3.hasMatch(plate);

    Vehicle? vehicle;

    setLoading(true);

    if (regex1.hasMatch(plate)) {
      plate = plate.replaceAll('-', '');
    }

    vehicle = await vehicleService.getVehicleFromPlate(plate);

    setLoading(false);

    if (vehicle != null) {
      if (!context.mounted) return;

      context.pushReplacement('/vehicleData', extra: vehicle);
      return;
    } else {
      if (!isValidPlate) {
        if (!context.mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Formato inválido, tente novamente!')),
        );
        return;
      }
      if (!context.mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Placa não encontrada, tente novamente!')),
      );
    }
  }

  Future<void> searchPlateFromImage(
    BuildContext context,
    XFile snapshotFile,
  ) async {
    final plate = await GetIt.instance<PlateService>().getPlateFromImage(
      snapshotFile,
    );
    if (!context.mounted) return;
    final success = await showPlatePreviewDialog(
      context,
      plate,
      needConfirmation: true,
    );
    if (success) {
      if (!context.mounted) return;
      searchController.text = plate;
      await searchPlateFromText(context);
    }
  }

  @override
  Future<Set<String>?> fetchSearchScope() async {
    final vehicleService = GetIt.instance<VehicleService>();

    List<String>? searchList = await vehicleService.getSearchHistory();

    // final searchList = ['BRA2E19', 'XYZ9876', 'JKL4567', 'MNO7890', 'PQR2345'];
    if (searchList != null) {
      searchScope =
          searchList.map((e) {
            return PlateFormater.formatPlate(e);
          }).toSet();
    }

    return searchScope ?? {};
  }
}
