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
      debugPrint('Login error: $e');
    } on Exception catch (e) {
      debugPrint('Login error: $e');
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

  Future<void> activateAccount(String newPassword) async {
    //TODO: Decidir se o resetToken vai pro SecureStorage ou passa direto pra ca
  }

  Future<void> forgotPassword(String username) async {
    // TODO
  }

  Future<void> resetPassword(String newPassword) async {
    //TODO: Decidir se o activateToken vai pro SecureStorage ou passa direto pra ca
  }

  Future<void> changePassword(String curPassword, String newPassword) async {
    // TODO
  }
}
