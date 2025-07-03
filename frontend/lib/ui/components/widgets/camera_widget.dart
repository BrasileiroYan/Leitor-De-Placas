import 'dart:typed_data';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/services/plate_service.dart';
import 'package:frontend/app/viewmodels/camera_viewmodel.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/widgets/dialogs/image_preview_dialog.dart';
import 'package:frontend/ui/components/widgets/dialogs/plate_preview_dialog.dart';
import 'package:frontend/ui/screens/confirmation_screen.dart';
import 'package:get_it/get_it.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

class CameraWidget extends StatefulWidget {
  final CameraDescription cameraDescription;
  const CameraWidget({super.key, required this.cameraDescription});

  @override
  State<CameraWidget> createState() => _CameraWidgetState();
}

class _CameraWidgetState extends State<CameraWidget> {
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
    final viewModel = Provider.of<CameraViewModel>(context);
    return AnimatedSwitcher(
      duration: const Duration(milliseconds: 750),
      child:
          (cameraController != null && cameraController!.value.isInitialized)
              ? Stack(
                children: [
                  Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    // crossAxisAlignment: CrossAxisAlignment.end,
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(4.0),
                        child: Row(
                          spacing: 8,
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: [
                            AspectRatioCycleButton(viewModel: viewModel),
                            FlashCycleButton(controller: cameraController!),
                          ],
                        ),
                      ),

                      Column(
                        // mainAxisSize: MainAxisSize.max,
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          if (viewModel.aspectRatio == AspectRatios.oneOne)
                            SizedBox(
                              height: MediaQuery.of(context).size.height / 12,
                            ),
                          ClipRect(
                            child: AspectRatio(
                              aspectRatio:
                                  viewModel.ratioValue, // Target aspect ratio
                              child: FittedBox(
                                fit: BoxFit.fitWidth,
                                child: SizedBox(
                                  width:
                                      cameraController!
                                          .value
                                          .previewSize!
                                          .height,
                                  height:
                                      cameraController!
                                          .value
                                          .previewSize!
                                          .width,
                                  child: SizedBox(
                                    child: CameraPreview(cameraController!),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                  Align(
                    alignment: Alignment.bottomCenter,
                    child: Container(
                      constraints: BoxConstraints(maxHeight: 72, maxWidth: 72),
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
              )
              : Center(child: CircularProgressIndicator()),
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
      if (cameraController!.value.isTakingPicture) return;
      XFile snapshotFile = await cameraController!.takePicture();
      Uint8List snapshotBytes = await snapshotFile.readAsBytes();
      if (!mounted) return;
      final goodImage = await showImagePreviewDialog(
        context,
        snapshotBytes,
        needConfirmation: true,
      );
      if (!mounted) return;
      if (goodImage) {
        await context.read<PlateSearchViewModel>().searchPlateFromImage(
          context,
          snapshotFile,
        );
      }
    }
  }
}
