import 'package:frontend/app/helpers/url_fallback.dart';

const String baseBackendUrl = String.fromEnvironment(
  'API_URL',
  defaultValue: '$urlFallback:8080',
);
const String pdiRequestUrl = String.fromEnvironment(
  'PDI_URL',
  defaultValue: '$urlFallback:5000',
);
