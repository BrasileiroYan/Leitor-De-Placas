import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/widgets/dialogs/request_camera_permission_dialog.dart.dart';
import 'package:frontend/ui/components/widgets/navigation_drawer_widget.dart';
import 'package:frontend/ui/components/widgets/plate_search_bar.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  bool _initialized = false;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (!_initialized) {
      _initialized = true;
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _checkCameraPermission(context);
      });
    }
  }

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
          IconButton(onPressed: () {}, icon: Icon(Icons.help_outline_rounded)),
        ],
        backgroundColor: AppColors.amber,
        title: viewModel.isSearching ? PlateSearchBar(viewModel) : null,
      ),
      drawer: NavigationDrawerWidget(),
      resizeToAvoidBottomInset: false,
      body: Ink(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.indigo.shade900, AppColors.bgColor],
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
          ),
        ),
        child: Stack(
          children: [
            Container(
              margin: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
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

  Future<void> _checkCameraPermission(BuildContext context) async {
    PermissionStatus cameraPermissionStatus = await Permission.camera.status;

    if (cameraPermissionStatus == PermissionStatus.denied) {
      if (context.mounted) {
        showRequestCameraPermissionDialog(context);
      }
    }
  }

  // Future<void> _handleCameraClicked(BuildContext context) async {
  //   List<CameraDescription> listCameras = await availableCameras();

  //   PermissionStatus cameraPermissionStatus = await Permission.camera.status;

  //   if (cameraPermissionStatus == PermissionStatus.denied) {
  //     if (!context.mounted) return;
  //     PermissionStatus? newStatus = await showRequestCameraPermissionDialog(
  //       context,
  //     );

  //     if (newStatus != null) {
  //       cameraPermissionStatus = newStatus;
  //     }
  //   }

  //   if (cameraPermissionStatus != PermissionStatus.denied &&
  //       cameraPermissionStatus != PermissionStatus.permanentlyDenied) {
  //     if (!context.mounted) return;

  //     int indexCameraDescription = listCameras.indexWhere(
  //       (e) => e.lensDirection == CameraLensDirection.back,
  //     );

  //     Navigator.push(
  //       context,
  //       MaterialPageRoute(
  //         builder:
  //             (context) => CameraScreen(
  //               cameraDescription: listCameras[indexCameraDescription],
  //             ),
  //       ),
  //     );
  //   } else if (cameraPermissionStatus == PermissionStatus.permanentlyDenied) {
  //     if (!context.mounted) return;
  //     await showDeniedCameraPermissionDialog(context);
  //   }
  // }

  // void _handleTextPlateSent(BuildContext context, String plate) {
  //   final regex1 = RegExp(r'^[A-Z]{3}-\d{4}$'); // Format: ABC-1234
  //   final regex2 = RegExp(r'^[A-Z]{3}\d[A-Z]\d{2}$'); // Format: ABC1C23
  //   if (regex1.hasMatch(plate) || regex2.hasMatch(plate)) {
  //     Navigator.push(
  //       context,
  //       MaterialPageRoute(builder: (context) => PlateDataScreen(plate)),
  //     );
  //   } else {}
  // }
}
