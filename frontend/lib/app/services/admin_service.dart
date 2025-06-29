import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/models/admin_actions.dart';
import 'package:frontend/app/models/app_user.dart';

class AdminService {
  final Dio _dio;
  AdminService(this._dio);

  List<AppUser> usersList = [];

  Future<bool> getUserPermissionLevel() async {
    try {
      final response = await _dio.get('/admin/users');
      if (response.statusCode == 200) {
        return true;
      }
    } catch (e) {
      debugPrint(e.toString());
    }
    return false;
  }

  Future<List<AppUser>> getAllUsers() async {
    try {
      final response = await _dio.get('/admin/users');
      if (response.statusCode == 200) {
        usersList =
            (response.data as List).map((e) => AppUser.fromMap(e)).toList();
        return usersList;
      }
    } on DioException catch (e) {
      debugPrint('\tERROR: ${e.message}');
    } on Exception {
      debugPrint("Unkown Exception");
    }
    debugPrint("\t Histórico vazio");
    return usersList;
  }

  Future<bool> updateUser(
    int id,
    String username,
    String password,
    AppUserRole role,
  ) async {
    try {
      final response = await _dio.put(
        '/admin/users/$id',
        data: {
          'username': username,
          'password': password,
          'role': role.name.toUpperCase(),
        },
      );

      return response.statusCode == 200;
    } on DioException catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    }
  }

  Future<bool> deleteUser(int id) async {
    try {
      final response = await _dio.delete('/admin/users/$id');

      return response.statusCode == 200;
    } on DioException catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    }
  }

  Future<bool> doAdminActionOnUser(int id, AdminActions action) async {
    try {
      final response = await _dio.put('/admin/users/$id/${action.name}');

      return response.statusCode == 204;
    } on DioException catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    }
  }

  Future<bool> createAndActivateUser(String username) async {
    try {
      final response = await _dio
          .clone(options: BaseOptions(receiveTimeout: Duration(seconds: 20)))
          .post(
            '/admin/users/create-user-and-activate',
            data: {'username': username},
          );

      return response.statusCode == 201;
    } on DioException catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    } on Exception catch (e) {
      debugPrint('\tAdmin error: $e');
      rethrow;
    }
  }
}
