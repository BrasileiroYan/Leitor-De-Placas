import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/models/vehicle.dart';

class VehicleService {
  final Dio _dio;
  VehicleService(this._dio);

  Future<List<Vehicle>> getAllVehicles() async {
    try {
      final response = await _dio.get('/vehicles');
      if (response.statusCode == 200) {
        List<Map<String, dynamic>> mapList = response.data;
        final vehicleList = mapList.map((e) => Vehicle.fromMap(e)).toList();
        return vehicleList;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception {
      debugPrint("Unkown Exception");
    }
    return [];
  }

  Future<Vehicle?> getVehicleFromPlate(String plate) async {
    try {
      final response = await _dio.get('/vehicles/$plate');
      if (response.statusCode == 200) {
        final vehicle = Vehicle.fromMap(response.data);
        return vehicle;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception {
      debugPrint("Unkown Exception");
    }
    debugPrint("Veículo com placa '$plate', não foi encontrado");
    return null;
  }

  Future<List<String>?> getSearchHistory() async {
    try {
      final response = await _dio.get('/history/scans?page=0&size=20');
      if (response.statusCode == 200) {
        final history = response.data.map((e) => e['scannedPlate']);
        if (history.isEmpty) return null;
        return history;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception {
      debugPrint("Unkown Exception");
    }
    debugPrint("Histórico vazio");
    return null;
  }
}
