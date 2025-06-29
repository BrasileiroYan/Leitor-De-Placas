import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend/app/app.dart';
import 'package:frontend/app/di/service_locator.dart';
import 'package:frontend/app/services/token_service.dart';
import 'package:frontend/app/viewmodels/admin_viewmodel.dart';
import 'package:frontend/app/viewmodels/login_viewmodel.dart';
import 'package:frontend/app/viewmodels/password_actions_viewmodel.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  ServiceLocator().setupLocator();
  await GetIt.instance<TokenService>().clearAll();
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (context) => LoginViewModel()),
        ChangeNotifierProvider(create: (context) => AdminViewModel()),
        ChangeNotifierProvider(create: (context) => PasswordActionsViewModel()),
        ChangeNotifierProvider(create: (context) => PlateSearchViewModel()),
      ],
      child: MainApp(),
    ),
  );
}
