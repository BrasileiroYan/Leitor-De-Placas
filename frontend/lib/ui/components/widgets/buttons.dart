import 'package:flutter/material.dart';

class PrimaryButton extends StatelessWidget {
  final String text;
  final IconData? icon;
  final Function onTap;
  final Color? bgColor;

  const PrimaryButton({
    super.key,
    required this.text,
    this.icon,
    required,
    required this.onTap,
    this.bgColor,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      borderRadius: BorderRadius.circular(16),
      onTap: () {
        onTap();
      },
      child: Ink(
        padding: EdgeInsets.symmetric(vertical: 8, horizontal: 32),
        decoration: BoxDecoration(
          color: bgColor ?? Colors.green,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              text,
              style: TextStyle(
                color: Colors.white,
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            icon != null
                ? Row(
                  children: [
                    SizedBox(width: 10),
                    Icon(icon, color: Color(0xFF1D0E44)),
                  ],
                )
                : Container(),
          ],
        ),
      ),
    );
  }
}
