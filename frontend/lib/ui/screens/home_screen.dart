import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/dialogs/denied_camera_permission_dialog.dart';
import 'package:frontend/ui/components/widgets/help_widget.dart';
import 'package:frontend/ui/components/widgets/dialogs/request_camera_permission_dialog.dart.dart';
import 'package:frontend/ui/screens/camera_screen.dart';
import 'package:frontend/ui/screens/vehicle_data_screen.dart';
import 'package:permission_handler/permission_handler.dart';

class HomeScreen extends StatelessWidget {
  final TextEditingController textController = TextEditingController();
  HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            onPressed: () {
              Navigator.pushReplacementNamed(context, "login");
            },
            icon: Icon(Icons.exit_to_app_outlined),
          ),
        ],
        backgroundColor: Colors.yellow.shade700,
        title: Text('Home Screen'),
        centerTitle: true,
      ),
      body: Ink(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.indigo.shade900, AppColors.bgColor],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
        child: Container(
          margin: const EdgeInsets.all(16),
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
                              _handleCameraClicked(context);
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
                          textCapitalization: TextCapitalization.characters,
                          controller: textController,
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
                                    _handleTextPlateSent(
                                      context,
                                      textController.text,
                                    );
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
                        SizedBox(height: 32),
                      ],
                    ),
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

  Future<void> _handleCameraClicked(BuildContext context) async {
    List<CameraDescription> listCameras = await availableCameras();

    PermissionStatus cameraPermissionStatus = await Permission.camera.status;

    if (cameraPermissionStatus == PermissionStatus.denied) {
      if (!context.mounted) return;
      PermissionStatus? newStatus = await showRequestCameraPermissionDialog(
        context,
      );

      if (newStatus != null) {
        cameraPermissionStatus = newStatus;
      }
    }

    if (cameraPermissionStatus != PermissionStatus.denied &&
        cameraPermissionStatus != PermissionStatus.permanentlyDenied) {
      if (!context.mounted) return;

      int indexCameraDescription = listCameras.indexWhere(
        (e) => e.lensDirection == CameraLensDirection.back,
      );

      Navigator.push(
        context,
        MaterialPageRoute(
          builder:
              (context) => CameraScreen(
                cameraDescription: listCameras[indexCameraDescription],
              ),
        ),
      );
    } else if (cameraPermissionStatus == PermissionStatus.permanentlyDenied) {
      if (!context.mounted) return;
      await showDeniedCameraPermissionDialog(context);
    }
  }

  void _handleTextPlateSent(BuildContext context, String plate) {
    final regex1 = RegExp(r'^[A-Z]{3}-\d{4}$'); // Format: ABC-1234
    final regex2 = RegExp(r'^[A-Z]{3}\d[A-Z]\d{2}$'); // Format: ABC1C23
    if (regex1.hasMatch(plate) || regex2.hasMatch(plate)) {
      Navigator.push(
        context,
        MaterialPageRoute(builder: (context) => PlateDataScreen(plate)),
      );
    } else {}
  }
}
