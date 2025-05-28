import 'package:flutter/material.dart';
import 'package:frontend/ui/_core/app_colors.dart';
import 'package:frontend/ui/_core/widgets/help_widget.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.yellow.shade700,
        title: Text('Home Screen'),
        centerTitle: true,
      ),
      backgroundColor: AppColors.bgColor,
      body: Padding(
        padding: EdgeInsets.all(20),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Stack(
            children: [
              Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(height: 32, width: double.infinity),
                    Column(
                      spacing: 8,
                      children: [
                        Text(
                          "Tire uma foto",
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        SizedBox(
                          height: 128,
                          width: 128,
                          child: IconButton(
                            onPressed: () {
                              Navigator.pushNamed(context, "camera");
                            },
                            icon: Icon(Icons.image_outlined, size: 96),
                            padding: EdgeInsets.zero,
                            style: ButtonStyle(
                              backgroundColor: WidgetStateColor.resolveWith((
                                states,
                              ) {
                                return Colors.yellow.shade700;
                              }),
                              shape: WidgetStateOutlinedBorder.resolveWith((
                                states,
                              ) {
                                return RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(16),
                                );
                              }),
                            ),
                          ),
                        ),
                        SizedBox(height: 32),
                      ],
                    ),
                    SizedBox(height: 32),
                    SizedBox(
                      height: 32,
                      child: Stack(
                        children: [
                          Center(
                            child: SizedBox(
                              height: 2,
                              width: double.infinity,
                              child: ColoredBox(color: Colors.black),
                            ),
                          ),
                          Center(
                            child: Container(
                              alignment: Alignment.center,
                              width: 60,
                              padding: EdgeInsets.symmetric(horizontal: 16),
                              color: Colors.white,
                              child: Center(
                                child: Text(
                                  "OU",
                                  style: TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    SizedBox(height: 32),
                    Column(
                      spacing: 8,
                      children: [
                        Text(
                          "Digite a placa",
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        TextFormField(
                          decoration: InputDecoration(
                            filled: true,
                            fillColor: Colors.grey.shade300,
                            suffixIconConstraints: BoxConstraints.tight(
                              Size(50, 48),
                            ),
                            suffixIcon: Row(
                              children: [
                                SizedBox(
                                  width: 2,
                                  height: double.infinity,
                                  child: Container(color: Colors.black),
                                ),
                                IconButton(
                                  padding: EdgeInsets.zero,
                                  onPressed: () {
                                    Navigator.pushNamed(context, "plate_data");
                                  },
                                  icon: Icon(Icons.send),
                                  style: ButtonStyle(
                                    shape:
                                        WidgetStateOutlinedBorder.resolveWith((
                                          states,
                                        ) {
                                          return BeveledRectangleBorder();
                                        }),
                                  ),
                                ),
                              ],
                            ),
                            border: OutlineInputBorder(
                              borderSide: BorderSide.none,
                            ),
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: 32),
                    Column(
                      spacing: 8,
                      children: [
                        Text(
                          "Selecione o modelo",
                          style: TextStyle(
                            fontSize: 20,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          spacing: 32,
                          children: [
                            Column(
                              spacing: 4,
                              children: [
                                ElevatedButton(
                                  onPressed: () {},
                                  style: ButtonStyle(
                                    foregroundColor: WidgetStatePropertyAll(
                                      Colors.black,
                                    ),
                                    backgroundColor:
                                        WidgetStateColor.resolveWith((states) {
                                          if (states.contains(
                                            WidgetState.disabled,
                                          )) {
                                            return Colors.grey;
                                          }
                                          return Colors.grey.shade300;
                                        }),
                                    shape: WidgetStatePropertyAll(
                                      RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(6),
                                      ),
                                    ),
                                  ),
                                  child: Text(
                                    "ABC1C34",
                                    style: TextStyle(
                                      fontFamily: "GL Nummernschild",
                                      fontSize: 25,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                                Text(
                                  "MERCOSUL",
                                  style: TextStyle(
                                    fontSize: 13,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ],
                            ),
                            Column(
                              spacing: 4,
                              children: [
                                ElevatedButton(
                                  onPressed: () {},
                                  style: ButtonStyle(
                                    foregroundColor: WidgetStatePropertyAll(
                                      Colors.black,
                                    ),
                                    backgroundColor:
                                        WidgetStateColor.resolveWith((states) {
                                          if (states.contains(
                                            WidgetState.disabled,
                                          )) {
                                            return Colors.grey;
                                          }
                                          return Colors.grey.shade300;
                                        }),
                                    shape: WidgetStatePropertyAll(
                                      RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(6),
                                      ),
                                    ),
                                  ),
                                  child: Text(
                                    "ABC-1234",
                                    style: TextStyle(
                                      fontFamily: "GL Nummernschild",
                                      fontSize: 25,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ),
                                Text(
                                  "UF - CIDADE",
                                  style: TextStyle(
                                    fontSize: 13,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ],
                    ),
                    SizedBox(height: 32),
                  ],
                ),
              ),
              HelpWidget(),
            ],
          ),
        ),
      ),
    );
  }
}
