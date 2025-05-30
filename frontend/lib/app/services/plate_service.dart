import 'package:frontend/app/models/vehicle.dart';

abstract class VehicleService {
  Future<Vehicle> fetchVehicle();
}

class ImageVehicleService implements VehicleService {
  Vehicle? _vehicle;
  @override
  Future<Vehicle> fetchVehicle() async {
    // TODO: fetch data from API with Multipart post
    return _vehicle!;
  }
}

class TextVehicleService implements VehicleService {
  Vehicle? _vehicle;
  @override
  Future<Vehicle> fetchVehicle() async {
    // TODO: fetch data from API
    return _vehicle!;
  }
}
