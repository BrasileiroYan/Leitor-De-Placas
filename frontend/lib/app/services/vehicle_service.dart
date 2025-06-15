// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';

import 'package:frontend/app/helpers/consts.dart';
import 'package:frontend/app/models/vehicle.dart';

abstract class VehicleService {
  Future<Vehicle> fetchVehicle();
}

class ImageVehicleService implements VehicleService {
  final dio = Dio();
  Vehicle? _vehicle;
  @override
  Future<Vehicle> fetchVehicle() async {
    // TODO: fetch data from API with Multipart post
    return _vehicle!;
  }
}

class TextVehicleService implements VehicleService {
  String query;
  final dio = Dio();
  Vehicle? _vehicle;

  TextVehicleService({required this.query});

  @override
  Future<Vehicle> fetchVehicle() async {
    try {
      Response response;
      response = await dio.get(
        vehicleTestUrl,
        options: Options(headers: requestHeader),
      );
      if (response.statusCode == 200) {
        _vehicle = Vehicle.fromJson(
          response.data["files"]["vehicle.json"]["content"],
        );
      } else {
        throw Exception(response.data);
      }
    } catch (e) {
      debugPrint(e.toString());
    }
    return _vehicle!;
  }
}
