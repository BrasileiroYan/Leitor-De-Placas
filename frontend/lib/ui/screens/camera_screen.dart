import 'dart:typed_data';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/image_preview_dialog.dart';

class CameraScreen extends StatefulWidget {
  final CameraDescription cameraDescription;
  const CameraScreen({super.key, required this.cameraDescription});

  @override
  State<CameraScreen> createState() => _CameraScreenState();
}

class _CameraScreenState extends State<CameraScreen> {
  CameraController? cameraController;

  @override
  void initState() {
    _initializeCamera();
    super.initState();
  }

  @override
  void dispose() {
    cameraController!.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.yellow.shade700,
        title: Text('Take a Picture'),
        centerTitle: true,
      ),
      backgroundColor: AppColors.bgColor,
      body: Center(
        child: Container(
          margin: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(16),
          ),
          child: ClipRRect(
            borderRadius: BorderRadius.circular(16),
            child: AnimatedSwitcher(
              duration: const Duration(milliseconds: 750),
              child:
                  (cameraController != null &&
                          cameraController!.value.isInitialized)
                      ? AspectRatio(
                        aspectRatio: 1 / cameraController!.value.aspectRatio,
                        child: CameraPreview(
                          cameraController!,
                          child: Stack(
                            children: [
                              Padding(
                                padding: const EdgeInsets.fromLTRB(
                                  16,
                                  0,
                                  16,
                                  128,
                                ),
                                child: Center(
                                  child: Image.asset(
                                    'assets/images/guides/plate_guide16.png',
                                  ),
                                ),
                              ),
                              Align(
                                alignment: Alignment.bottomCenter,
                                child: Container(
                                  constraints: BoxConstraints(
                                    maxHeight: 64,
                                    maxWidth: 64,
                                  ),
                                  margin: EdgeInsets.only(bottom: 32),
                                  decoration: BoxDecoration(
                                    shape: BoxShape.circle,
                                    color: Colors.grey,
                                  ),
                                  child: GestureDetector(
                                    onTap: () {
                                      _onCaptureButtonClicked();
                                    },
                                    child: Container(
                                      margin: EdgeInsets.all(4),
                                      decoration: BoxDecoration(
                                        shape: BoxShape.circle,
                                        color: Colors.white,
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      )
                      : Center(child: CircularProgressIndicator()),
            ),
          ),
        ),
      ),
    );
  }

  Future<void> _initializeCamera() async {
    cameraController = CameraController(
      widget.cameraDescription,
      ResolutionPreset.high,
      enableAudio: false,
    );

    await cameraController!.initialize();
    setState(() {});
  }

  _onCaptureButtonClicked() async {
    if (cameraController != null) {
      XFile snapshotFile = await cameraController!.takePicture();
      Uint8List snapshotBytes = await snapshotFile.readAsBytes();
      if (!mounted) return;
      await showImagePreviewDialog(
        context,
        snapshotBytes,
        needConfirmation: true,
      );
      if (!mounted) return;
      // Navigator.pushNamed(context, "confirmation");
    }
  }
}
