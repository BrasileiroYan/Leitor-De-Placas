import 'package:frontend/app/helpers/url_fallback.dart';

const String baseBackendUrl =
    '${const String.fromEnvironment('API_URL', defaultValue: urlFallback)}:8080';
const String pdiRequestUrl =
    '${const String.fromEnvironment('API_URL', defaultValue: urlFallback)}:5000';
