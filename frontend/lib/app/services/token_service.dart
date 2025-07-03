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
}
