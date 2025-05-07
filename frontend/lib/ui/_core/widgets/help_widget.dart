import 'package:flutter/material.dart';

class HelpWidget extends StatelessWidget {
  final bool lightMode;
  const HelpWidget({super.key, lightMode}) : lightMode = lightMode ?? false;

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        SizedBox(
          width: 48,
          height: 48,
          child: Icon(
            Icons.support_agent_rounded,
            color: lightMode ? Colors.white : Colors.black,
          ),
        ),
        Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Text(
              "Está com problemas?",
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.bold,
                color: lightMode ? Colors.white : Colors.black,
              ),
            ),
            SizedBox(
              height: 2,
              width: 152,
              child: Container(color: lightMode ? Colors.white : Colors.black),
            ),
            SizedBox(
              height: 24,
              child: TextButton(
                onPressed: () {},
                style: ButtonStyle(
                  padding: WidgetStatePropertyAll(EdgeInsets.zero),
                  minimumSize: WidgetStatePropertyAll(Size(0, 0)),
                  tapTargetSize: MaterialTapTargetSize.shrinkWrap,
                ),
                child: Text(
                  "Contate-nos",
                  style: TextStyle(
                    color: Colors.blue,
                    decoration: TextDecoration.underline,
                  ),
                ),
              ),
            ),
          ],
        ),
      ],
    );
  }
}
