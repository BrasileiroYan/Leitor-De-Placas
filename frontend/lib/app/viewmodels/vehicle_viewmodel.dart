import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/app/services/vehicle_service.dart';
import 'package:get_it/get_it.dart';

class VehicleViewModel {
  Vehicle? _vehicle;
  Future<Vehicle> getVehicleFromImage() async {
    //TODO: Review code for better utilization
    return _vehicle ?? Vehicle.getExample();
  }

  Future<Vehicle> getVehicleFromText(String query) async {
    final vehicleService = GetIt.instance<VehicleService>();
    _vehicle = await vehicleService.getVehicleFromPlate(query);
    return _vehicle ?? Vehicle.getExample();
  }

  Vehicle get vehicle => _vehicle ?? Vehicle.getExample();
}
