import 'package:flutter/material.dart';
import 'package:frontend/app/models/vehicle.dart';
import 'package:frontend/ui/screens/first_login_screen.dart';
import 'package:frontend/ui/screens/home_screen.dart';
import 'package:frontend/ui/screens/login_screen.dart';
import 'package:frontend/ui/screens/password_recovery_screen.dart';
import 'package:frontend/ui/screens/vehicle_data_screen.dart';
import 'package:go_router/go_router.dart';

class MainApp extends StatelessWidget {
  final _router = GoRouter(
    initialLocation: '/',
    routes: [
      GoRoute(path: '/', builder: (context, state) => const LoginScreen()),
      GoRoute(
        path: '/passwordRecovery',
        builder: (context, state) => PasswordRecoveryScreen(),
      ),
      GoRoute(path: '/home', builder: (context, state) => HomeScreen()),
      GoRoute(
        path: '/vehicleData',
        builder: (context, state) {
          final vehicle = state.extra as Vehicle;
          return VehicleDataScreen(vehicle);
        },
      ),
      GoRoute(
        path: '/firstLogin',
        builder: (context, state) {
          final token = state.uri.queryParameters['token'];
          return FirstLoginScreen(token!);
        },
      ),
      GoRoute(
        path: '/activate-account',
        builder: (context, state) {
          final token = state.uri.queryParameters['token'];
          Future.microtask(() {
            if (context.mounted) {
              context.go('/');
              context.push('/firstLogin?token=$token');
            }
          });
          return Scaffold(body: Center(child: CircularProgressIndicator()));
        },
      ),
    ],
  );

  MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: _router,
      title: "SFA",

      // home: LoginScreen(),
    );
  }
}
