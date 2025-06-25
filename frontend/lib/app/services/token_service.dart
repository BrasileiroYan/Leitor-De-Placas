import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class TokenService {
  final FlutterSecureStorage _storage;

  TokenService(this._storage);

  static const _tokenKey = 'token';
  static const _refreshTokenKey = 'refreshToken';

  Future<void> saveTokens({
    required String token,
    required String refreshToken,
  }) async {
    await _storage.write(key: _tokenKey, value: token);
    await _storage.write(key: _refreshTokenKey, value: refreshToken);
  }

  Future<String?> getToken() async => await _storage.read(key: _tokenKey);

  Future<String?> getRefreshToken() async =>
      await _storage.read(key: _refreshTokenKey);

  Future<void> clearAll() async => await _storage.deleteAll();

  // Future<void> saveToken(String token) async => await _storage.write(
  //   key: _accessTokenKey,
  //   value: token,
  //   aOptions: _getAndroidOptions(),
  // );

  // Future<void> saveRefreshToken(String token) async => await _storage.write(
  //   key: _refreshTokenKey,
  //   value: token,
  //   aOptions: _getAndroidOptions(),
  // );

  // Future<String?> getToken() async =>
  //     await _storage.read(key: _accessTokenKey, aOptions: _getAndroidOptions());

  // Future<String?> getRefreshToken() async => await _storage.read(
  //   key: _refreshTokenKey,
  //   aOptions: _getAndroidOptions(),
  // );
}

// class TokenService {
//   static final TokenService _instance = TokenService._();
//   factory TokenService() => _instance;
//   TokenService._();

//   String? _token;
//   String? _refreshToken;

//   String? getToken() => _token;

//   Future<void> saveTokens({required String token, required String refresh}) async {
//     // persist to secure storage
//     _token = token;
//     _refreshToken = refresh;
//   }

//   Future<String?> refreshToken() async {
//     // e.g. call your /auth/refresh endpoint directly (you could use a
//     // separate Dio instance here, without interceptors, to avoid loops).
//     final response = await Dio().post(
//       'https://localhost:8080/auth/refresh',
//       data: {'refreshToken': _refreshToken},
//     );
//     final newToken = response.data['token'] as String;
//     await saveTokens(token: newToken, refresh: _refreshToken!);
//     return newToken;
//   }
// }
