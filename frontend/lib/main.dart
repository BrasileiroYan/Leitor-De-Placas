import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/app/app.dart';
import 'package:frontend/app/di/service_locator.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  ServiceLocator().setupLocator();
  runApp(const MainApp());
}
