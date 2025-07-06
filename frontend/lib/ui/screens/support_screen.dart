import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_background_gradient.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/dialogs/help_dialog.dart';
import 'package:frontend/ui/components/widgets/navigation_drawer_widget.dart';

class SupportScreen extends StatelessWidget {
  final bool loggedIn;
  const SupportScreen({super.key, required this.loggedIn});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            onPressed: () {
              showHelpDialog(context);
            },
            icon: Icon(Icons.help_outline_rounded),
          ),
        ],
        backgroundColor: AppColors.amber,
        title: Text("Suporte"),
        centerTitle: true,
      ),
      drawer: loggedIn ? NavigationDrawerWidget() : null,
      body: AppBackgroundGradient(
        padding: EdgeInsets.all(16),
        child: Ink(
          color: Colors.white,
          child: Center(
            child: Text(
              "WIP",
              style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),
            ),
          ),
        ),
      ),
    );
  }
}
