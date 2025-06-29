import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:go_router/go_router.dart';

class NavigationDrawerWidget extends StatelessWidget {
  const NavigationDrawerWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [_drawerHeader(context), _drawerMenuItems(context)],
        ),
      ),
    );
  }

  Widget _drawerHeader(BuildContext context) {
    return Container(
      color: AppColors.amber,
      padding: EdgeInsets.only(
        top: 24 + MediaQuery.of(context).padding.top,
        bottom: 24,
      ),
      child: Row(
        spacing: 8,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Image.asset("assets/images/app_icon.png", height: 32),
          Text(
            'ScannerFA',
            style: TextStyle(
              fontFamily: 'Italiana',
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }

  Widget _drawerMenuItems(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(24),
      child: Wrap(
        runSpacing: 8,
        children: [
          ListTile(
            leading: const Icon(Icons.home),
            title: const Text("Tela Inicial"),
            onTap: () {
              context.go('/home');
            },
          ),
          Visibility(
            visible: true,
            child: ListTile(
              leading: const Icon(Icons.admin_panel_settings),
              title: const Text("Admin"),
              onTap: () {},
            ),
          ),
          ListTile(
            leading: const Icon(Icons.settings_rounded),
            title: const Text("Configurações"),
            onTap: () {},
          ),
          Divider(color: Colors.black),
          ListTile(
            leading: const Icon(Icons.logout),
            title: const Text("Sair"),
            onTap: () {
              context.go('/');
            },
          ),
        ],
      ),
    );
  }
}
