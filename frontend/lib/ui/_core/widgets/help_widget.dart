import 'package:flutter/material.dart';

class HelpWidget extends StatelessWidget {
  final bool lightMode;
  final double size;
  const HelpWidget({super.key, lightMode, size})
    : lightMode = lightMode ?? false,
      size = size ?? 0.7;

  @override
  Widget build(BuildContext context) {
    return Container(
      alignment: Alignment.bottomLeft,
      margin: EdgeInsets.all(4),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          SizedBox(
            width: 48 * size,
            height: 48 * size,
            child: Icon(
              Icons.support_agent_rounded,
              color: lightMode ? Colors.white : Colors.black,
              size: 40 * size,
            ),
          ),
          Column(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              Text(
                "Está com problemas?",
                style: TextStyle(
                  fontSize: 12 * size,
                  fontWeight: FontWeight.bold,
                  color: lightMode ? Colors.white : Colors.black,
                ),
              ),
              SizedBox(
                height: 2 * size,
                width: 152 * size,
                child: Container(
                  color: lightMode ? Colors.white : Colors.black,
                ),
              ),
              SizedBox(
                height: 24 * size,
                child: GestureDetector(
                  onTap: () {},
                  child: Text(
                    "Contate-nos",
                    style: TextStyle(
                      color: Colors.blue,
                      decoration: TextDecoration.underline,
                      fontSize: 12 * size,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
