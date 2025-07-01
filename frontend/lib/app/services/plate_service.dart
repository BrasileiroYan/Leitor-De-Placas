import 'package:camera/camera.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/helpers/consts.dart';

class PlateService {
  final Dio _dio;
  PlateService(this._dio);

  Future<String> getPlateFromImage(XFile imageFile) async {
    final multipartFile = await MultipartFile.fromFile(
      imageFile.path,
      filename: 'placa.jpg',
      contentType: DioMediaType('image', 'jpeg'),
    );
    final formData = FormData.fromMap({'file': multipartFile});
    debugPrint("\tNÃO CHEGOU DENTRO TO TRY CATCH");

    try {
      final response = await Dio().post(
        '$pdiRequestUrl/processar/',
        data: formData,
        // options: Options(headers: {'Content-Type': 'multipart/form-data'}),
      );

      if (response.statusCode == 200) {
        final plate = response.data;
        debugPrint("\t$plate");
        return plate;
      }
    } on DioException catch (e) {
      debugPrint(e.message);
    } on Exception {
      debugPrint("Unkown Exception");
    }
    return 'N/A';
  }
}
