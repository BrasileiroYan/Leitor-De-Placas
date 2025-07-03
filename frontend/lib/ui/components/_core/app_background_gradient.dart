import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';

class AppBackgroundGradient extends StatelessWidget {
  final Widget? child;
  final EdgeInsets? padding;
  const AppBackgroundGradient({super.key, this.child, this.padding});

  @override
  Widget build(BuildContext context) {
    return Ink(
      padding: padding,
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [AppColors.lightBgColor, AppColors.bgColor],
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
        ),
      ),
      child: child,
    );
  }
}
