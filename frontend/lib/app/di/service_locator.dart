import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:frontend/app/helpers/auth_interceptor.dart';
import 'package:frontend/app/helpers/consts.dart';
import 'package:frontend/app/services/admin_service.dart';
import 'package:frontend/app/services/auth_service.dart';
import 'package:frontend/app/services/plate_service.dart';
import 'package:frontend/app/services/token_service.dart';
import 'package:frontend/app/services/vehicle_service.dart';
import 'package:get_it/get_it.dart';

class ServiceLocator {
  final GetIt locator = GetIt.instance;

  void setupLocator() {
    locator.registerLazySingleton<FlutterSecureStorage>(
      () => FlutterSecureStorage(
        aOptions: const AndroidOptions(encryptedSharedPreferences: true),
      ),
    );

    locator.registerLazySingleton<TokenService>(
      () => TokenService(locator<FlutterSecureStorage>()),
    );

    locator.registerLazySingleton<Dio>(() {
      final dio = Dio(
        BaseOptions(
          baseUrl: baseBackendUrl, // localhost refered from emulator
          contentType: 'application/json',
          responseType: ResponseType.json,
          connectTimeout: const Duration(seconds: 5),
          receiveTimeout: const Duration(seconds: 3),
        ),
      );
      dio.interceptors.add(AuthInterceptor());
      return dio;
    });

    locator.registerLazySingleton<AuthService>(
      () => AuthService(locator<Dio>()),
    );

    locator.registerLazySingleton<VehicleService>(
      () => VehicleService(locator<Dio>()),
    );

    locator.registerLazySingleton<AdminService>(
      () => AdminService(locator<Dio>()),
    );

    locator.registerLazySingleton<PlateService>(() => PlateService());
  }
}
