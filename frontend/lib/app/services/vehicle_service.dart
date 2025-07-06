import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:open_file/open_file.dart';
import 'package:path_provider/path_provider.dart';

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
        await _dio.post('/scans/register', data: {'plate': plate});
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
      final response = await _dio.get('/scans/history?page=0&size=20');
      if (response.statusCode == 200) {
        final history = response.data['content'] as List;
        final platesList =
            history.map((e) => e['scannedPlate'].toString()).toList();
        if (platesList.isEmpty) return null;
        return platesList;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception {
      debugPrint("Unkown Exception");
    }
    debugPrint("Histórico vazio");
    return null;
  }

  Future<bool> downloadVehiclePDF(String plate) async {
    final dir = await getApplicationDocumentsDirectory();
    final filePath = '${dir.path}/${plate}_SFA.pdf';
    debugPrint('TO SENDO CHAMADO');
    try {
      final response = await _dio.download(
        '/vehicles/$plate/download-pdf',
        filePath,
        // options: Options(responseType: ResponseType.bytes),
      );
      if (response.statusCode == 200) {
        // debugPrint(response.toString());
        await OpenFile.open(filePath, type: 'application/pdf');
        debugPrint(filePath);
        return true;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception catch (e) {
      debugPrint(e.toString());
      debugPrint("Unkown Exception");
    }
    debugPrint("Veículo com placa '$plate', não foi encontrado");
    return false;
  }
}
