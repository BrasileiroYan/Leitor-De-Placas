import 'package:frontend/app/helpers/url_fallback.dart';

const baseUrl = String.fromEnvironment('API_URL', defaultValue: urlFallback);

const String baseBackendUrl = "$baseUrl:8080";
const String pdiRequestUrl = "$baseUrl:5000";
