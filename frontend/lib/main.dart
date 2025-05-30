import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/app/app.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);

  runApp(const MainApp());
}
