import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/services/token_service.dart';
import 'package:get_it/get_it.dart';

class AuthService {
  final Dio _dio;
  final _tokenService = GetIt.instance<TokenService>();
  AuthService(this._dio);

  Future<bool> login(String username, String password) async {
    try {
      final response = await _dio.post(
        '/auth/login',
        data: {'username': username, 'password': password},
      );

      if (response.statusCode == 200) {
        final token = response.data['token'];
        final refreshToken = response.data['refreshToken'];

        await _tokenService.saveTokens(
          token: token,
          refreshToken: refreshToken,
        );
        return true;
      }
    } on DioException catch (e) {
      debugPrint('\tLogin error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tLogin error: $e');
      rethrow;
    }
    return false;
  }

  Future<void> logout() async {
    final token = await _tokenService.getToken();
    if (token != null) {
      try {
        await _dio.post('/auth/logout');
      } catch (_) {}
    }
    await _tokenService.clearAll();
  }

  Future<String> activateAccount(String token, String newPassword) async {
    try {
      // debugPrint(token);
      final response = await _dio.post(
        '/auth/activate-account',
        data: {'token': token, 'newPassword': newPassword},
      );

      if (response.statusCode == 200) {
        final message = response.data['message'];
        if (message == null) return '';
        return message;
      }
    } on DioException catch (e) {
      debugPrint('\tAuth error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tAuth error: $e');
      rethrow;
    }
    return '';
  }

  Future<bool> forgotPassword(String username) async {
    try {
      final response = await _dio.post(
        '/auth/forgot-password',
        data: {'username': username},
      );

      if (response.statusCode == 200) {
        final message = response.data['message'];

        debugPrint("\t$message");
        return true;
      }
    } on DioException catch (e) {
      debugPrint('\tLogin error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tLogin error: $e');
      rethrow;
    }
    return false;
  }

  Future<String> resetPassword(String token, String newPassword) async {
    try {
      debugPrint("\t  TOKEN: $token");
      final response = await _dio.post(
        '/auth/reset-password',
        data: {'resetToken': token, 'newPassword': newPassword},
      );

      if (response.statusCode == 200) {
        final message = response.data['message'];
        if (message == null) return '';
        return message;
      }
    } on DioException catch (e) {
      debugPrint('\tAuth error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tAuth error: $e');
      rethrow;
    }
    return '';
  }

  Future<void> changePassword(String curPassword, String newPassword) async {
    // TODO
  }
}
