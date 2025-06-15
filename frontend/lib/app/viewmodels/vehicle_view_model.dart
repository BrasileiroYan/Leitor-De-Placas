import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/app/services/vehicle_service.dart';

class VehicleViewModel {
  Vehicle _vehicle = Vehicle.getExample();
  Future<Vehicle> getVehicleFromImage() async {
    //TODO: Review code for better utilization
    final VehicleService service = ImageVehicleService();
    _vehicle = await service.fetchVehicle();
    return _vehicle;
  }

  Future<Vehicle> getVehicleFromText(String query) async {
    //TODO: Review code for better utilization
    final VehicleService service = TextVehicleService(query: query);
    _vehicle = await service.fetchVehicle();
    return _vehicle;
  }

  Vehicle get vehicle => _vehicle;
}
