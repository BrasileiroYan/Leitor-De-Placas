import 'package:camera/camera.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';

class PlateService {
  final Dio _dio;
  PlateService(this._dio);

  Future<String?> getPlateFromImage(
    XFile imageFile, {
    bool isAntiga = false,
  }) async {
    final multipartFile = await MultipartFile.fromFile(
      imageFile.path,
      filename: '${isAntiga ? 'antiga' : 'nova'}.jpg',
    );
    final formData = FormData.fromMap({'image': multipartFile});

    try {
      final response = await _dio.post(
        '/ocr/read',
        data: formData,
        options: Options(headers: {'Content-Type': 'multipart/form-data'}),
      );

      if (response.statusCode == 200) {
        final plate = response.data['plate'];
        return plate;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception {
      debugPrint("Unkown Exception");
    }
    return null;
  }
}
