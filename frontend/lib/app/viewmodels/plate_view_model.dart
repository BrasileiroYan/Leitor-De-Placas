import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/app/services/plate_service.dart';

class PlateViewModel {
  Vehicle _vehicle = Vehicle();
  Future<Vehicle> getVehicleFromImage() async {
    //TODO: Review code for better utilization
    final VehicleService service = ImageVehicleService();
    _vehicle = await service.fetchVehicle();
    return _vehicle;
  }

  Future<Vehicle> getVehicleFromText(String plateText) async {
    //TODO: Review code for better utilization
    final VehicleService service = TextVehicleService();
    _vehicle = await service.fetchVehicle();
    return _vehicle;
  }
}
