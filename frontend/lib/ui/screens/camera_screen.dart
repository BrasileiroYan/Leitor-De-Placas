import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';

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
      body: Container(
        margin: const EdgeInsets.all(20),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
        ),
        child:
            (cameraController != null && cameraController!.value.isInitialized)
                ? ClipRRect(
                  borderRadius: BorderRadius.circular(16),
                  child: Stack(
                    fit: StackFit.expand,
                    children: [
                      // CameraPreview fills and overflows, but gets clipped to borderRadius
                      Positioned.fill(
                        child: FittedBox(
                          fit:
                              BoxFit
                                  .cover, // ✅ overflows and fills the container like a background
                          child: SizedBox(
                            width: cameraController!.value.previewSize!.height,
                            height: cameraController!.value.previewSize!.width,
                            child: CameraPreview(cameraController!),
                          ),
                        ),
                      ),
                      // Overlay guide on top (optional)
                      Image.asset(
                        'assets/images/guides/plate_guide16.png',
                        fit: BoxFit.fitWidth,
                      ),

                      Align(
                        alignment: Alignment.bottomCenter,
                        child: Container(
                          margin: EdgeInsets.only(bottom: 32),
                          width: 64,
                          height: 64,
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            color: Colors.grey,
                          ),
                          child: GestureDetector(
                            onTap: () {
                              Navigator.pushNamed(context, "confirmation");
                            },
                            child: Container(
                              margin: EdgeInsets.all(4),
                              width: 30,
                              height: 30,
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
                )
                : Center(child: CircularProgressIndicator()),
      ),
    );

    // return Scaffold(
    //   appBar: AppBar(
    //     backgroundColor: Colors.yellow.shade700,
    //     title: Text('Take a Picture'),
    //     centerTitle: true,
    //   ),
    //   backgroundColor: AppColors.bgColor,
    //   body: Container(
    //     margin: const EdgeInsets.all(20),
    //     decoration: BoxDecoration(
    //       color: Colors.white,
    //       borderRadius: BorderRadius.circular(16),
    //     ),
    //     child: Center(
    //       child: AnimatedSwitcher(
    //         duration: const Duration(milliseconds: 750),
    //         child:
    //             (cameraController != null &&
    //                     cameraController!.value.isInitialized)
    //                 ? AspectRatio(
    //                   aspectRatio: 1 / cameraController!.value.aspectRatio,
    //                   child: CameraPreview(
    //                     cameraController!,
    //                     child: Image.asset(
    //                       'assets/images/guides/plate_guide16.png',
    //                     ),
    //                   ),
    //                 )
    //                 : CircularProgressIndicator(),
    //       ),
    //     ),
    //   ),
    // );

    // body: Padding(
    //   padding: EdgeInsets.all(20),
    //   child: Container(
    //     decoration: BoxDecoration(
    //       color: Colors.white,
    //       borderRadius: BorderRadius.circular(20),
    //     ),
    //     child: Stack(
    //       children: [
    //         Padding(
    //           padding: const EdgeInsets.all(16.0),
    //           child: Column(
    //             mainAxisAlignment: MainAxisAlignment.center,
    //             crossAxisAlignment: CrossAxisAlignment.center,
    //             children: [
    //               SizedBox(height: 32, width: double.infinity),
    //               IconButton(
    //                 onPressed: () {
    //                   Navigator.pushNamed(context, "confirmation");
    //                 },
    //                 icon: Icon(Icons.camera),
    //               ),
    //             ],
    //           ),
    //         ),
    //       ],
    //     ),
    //   ),
    // ),
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
}
