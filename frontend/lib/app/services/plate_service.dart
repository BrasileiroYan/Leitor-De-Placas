import 'package:camera/camera.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/helpers/consts.dart';

class PlateService {
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
      );

      if (response.statusCode == 200) {
        final plate = response.data;
        debugPrint("\t$plate");
        return plate;
      } else if (response.statusCode == 204) {
        throw DioException(
          requestOptions: response.requestOptions,
          response: response,
          message: "Texto não encontrado na placa",
        );
      }
    } on DioException catch (e) {
      debugPrint(e.message);
      rethrow;
    } on Exception {
      debugPrint("Unkown Exception");
    }
    return 'N/A';
  }
}
