import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/_core/app_background_gradient.dart';
import 'package:frontend/ui/components/widgets/buttons.dart';
import 'package:frontend/ui/components/widgets/dialogs/denied_camera_permission_dialog.dart';
import 'package:frontend/ui/components/widgets/dialogs/help_dialog.dart';
import 'package:frontend/ui/components/widgets/dialogs/request_camera_permission_dialog.dart.dart';
import 'package:frontend/ui/components/widgets/navigation_drawer_widget.dart';
import 'package:frontend/ui/components/widgets/plate_search_bar.dart';
import 'package:frontend/ui/components/widgets/camera_widget.dart';
// import 'package:go_router/go_router.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<PlateSearchViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            icon: Icon(viewModel.isSearching ? Icons.close : Icons.search),
            onPressed: () {
              viewModel.setSearching(!viewModel.isSearching);
              if (!viewModel.isSearching) {
                viewModel.searchController.text = ' ';
              }
            },
          ),
          IconButton(
            onPressed: () {
              showHelpDialog(context);
            },
            icon: Icon(Icons.help_outline_rounded),
          ),
        ],
        backgroundColor: AppColors.amber,
        title: viewModel.isSearching ? PlateSearchBar(viewModel) : null,
      ),
      drawer: NavigationDrawerWidget(),
      resizeToAvoidBottomInset: false,
      body: AppBackgroundGradient(
        child: Stack(
          children: [
            Padding(
              padding: const EdgeInsets.all(16),
              child: Ink(
                padding: EdgeInsets.all(4),
                decoration: BoxDecoration(
                  color: Colors.grey.shade900,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Ink(
                  decoration: BoxDecoration(
                    color: Colors.black,
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: FutureBuilder(
                    future: _handleCameraPermission(context),
                    builder: (context, snapshot) {
                      if (snapshot.hasData) {
                        final cameraDescription = snapshot.data!;
                        return CameraWidget(
                          cameraDescription: cameraDescription,
                        );
                      } else {
                        return FutureBuilder(
                          future: Future.delayed(Duration(seconds: 2), () {
                            return true;
                          }),
                          builder: (context, snapshot) {
                            if (snapshot.hasData) {
                              return Padding(
                                padding: const EdgeInsets.all(32.0),
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(
                                      "Sem permissão para acessar a câmera",
                                      style: TextStyle(
                                        fontSize: 20,
                                        fontWeight: FontWeight.bold,
                                        color: Colors.white,
                                      ),
                                      textAlign: TextAlign.center,
                                    ),
                                    Text(
                                      "Para capturar a placa, por favor permita que o aplicativo acesse a câmera do aparelho",
                                      style: TextStyle(color: Colors.white),
                                      textAlign: TextAlign.center,
                                    ),
                                    SizedBox(height: 24),
                                    PrimaryButton(
                                      text: "Pedir acesso",
                                      bgColor: AppColors.lightAmber,
                                      onTap: () {
                                        setState(() {});
                                      },
                                    ),
                                  ],
                                ),
                              );
                            } else {
                              return Center(child: CircularProgressIndicator());
                            }
                          },
                        );
                      }
                    },
                  ),
                ),
              ),
            ),
            viewModel.isLoading
                ? AnimatedSwitcher(
                  duration: const Duration(milliseconds: 750),
                  child: Container(
                    margin: EdgeInsets.only(
                      bottom: MediaQuery.of(context).padding.bottom,
                    ),
                    width: MediaQuery.of(context).size.width,
                    height: MediaQuery.of(context).size.height,
                    color: Colors.black.withAlpha((255 / 2).round()),
                    child: Center(child: CircularProgressIndicator()),
                  ),
                )
                : SizedBox.shrink(),
          ],
        ),
      ),
    );
  }

  Future<CameraDescription?> _handleCameraPermission(
    BuildContext context,
  ) async {
    List<CameraDescription> listCameras = await availableCameras();

    PermissionStatus cameraPermissionStatus = await Permission.camera.status;

    if (cameraPermissionStatus == PermissionStatus.denied) {
      if (!context.mounted) return null;
      PermissionStatus? newStatus = await showRequestCameraPermissionDialog(
        context,
      );

      if (newStatus != null) {
        cameraPermissionStatus = newStatus;
      } else {
        return null;
      }
    }

    if (cameraPermissionStatus != PermissionStatus.denied &&
        cameraPermissionStatus != PermissionStatus.permanentlyDenied) {
      if (!context.mounted) return null;

      int indexCameraDescription = listCameras.indexWhere(
        (e) => e.lensDirection == CameraLensDirection.back,
      );

      return listCameras[indexCameraDescription];
    } else if (cameraPermissionStatus == PermissionStatus.permanentlyDenied) {
      if (!context.mounted) return null;
      await showDeniedCameraPermissionDialog(context);
    }
    return null;
  }
}
