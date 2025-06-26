import 'package:dio/dio.dart';
import 'package:frontend/app/services/token_service.dart';
import 'package:get_it/get_it.dart';

class AuthInterceptor extends Interceptor {
  final _tokenService = GetIt.instance<TokenService>();

  AuthInterceptor();

  @override
  void onRequest(
    RequestOptions options,
    RequestInterceptorHandler handler,
  ) async {
    final token = await _tokenService.getToken();
    if (token != null) {
      options.headers['Authorization'] = 'Bearer $token';
    }
    return handler.next(options);
  }

  @override
  void onError(DioException err, ErrorInterceptorHandler handler) async {
    if (err.response?.statusCode == 401) {
      final refreshToken = await _tokenService.getRefreshToken();
      if (refreshToken == null) return handler.next(err);

      try {
        final response = await Dio().post(
          'http://10.0.2.2:8080/auth/refresh',
          data: {"refreshToken": refreshToken},
        );

        final newToken = response.data['token'];
        final newRefresh = response.data['refreshToken'];

        await _tokenService.saveTokens(
          token: newToken,
          refreshToken: newRefresh,
        );

        final clonedRequest =
            err.requestOptions..headers['Authorization'] = 'Bearer $newToken';

        final retryResponse = await Dio().fetch(clonedRequest);
        return handler.resolve(retryResponse);
      } catch (_) {
        await _tokenService.clearAll();
        return handler.next(err);
      }
    }

    return handler.next(err);
  }
}
