import 'package:frontend/app/models/vehicle.dart';

class VehicleViewModel {
  Vehicle _vehicle = Vehicle.getExample();
  Future<Vehicle> getVehicleFromImage() async {
    //TODO: Review code for better utilization
    return _vehicle;
  }

  Future<Vehicle> getVehicleFromText(String query) async {
    //TODO: Review code for better utilization
    return _vehicle;
  }

  Vehicle get vehicle => _vehicle;
}
