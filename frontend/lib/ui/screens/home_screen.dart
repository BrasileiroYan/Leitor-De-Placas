import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/screens/login_screen.dart';
import 'package:go_router/go_router.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final searchViewModel = SearchViewModel();
  bool _isSearching = false;
  final FocusNode _focusNode = FocusNode();

  @override
  void initState() {
    searchViewModel.addListener(() {
      setState(() {});
    });
    super.initState();
  }

  @override
  void dispose() {
    _focusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            icon: Icon(_isSearching ? Icons.close : Icons.search),
            onPressed: () {
              setState(() {
                _isSearching = !_isSearching;
                if (!_isSearching) {
                  searchViewModel.searchController.text = '';
                  _focusNode.unfocus();
                }
              });
            },
          ),
          IconButton(onPressed: () {}, icon: Icon(Icons.help_outline_rounded)),
        ],
        backgroundColor: AppColors.amber,
        title:
            _isSearching
                ? TextField(
                  controller: searchViewModel.searchController,
                  textCapitalization: TextCapitalization.characters,
                  focusNode: _focusNode,
                  autofocus: true,
                  decoration: InputDecoration(hintText: 'Pesquisar placa...'),
                  style: TextStyle(color: Colors.white),
                  onChanged: (value) {
                    searchViewModel.searchController.value = searchViewModel
                        .searchController
                        .value
                        .copyWith(
                          text: value.toUpperCase(),
                          selection: TextSelection.collapsed(
                            offset: value.length,
                          ),
                        );
                  },
                  onSubmitted:
                      (value) => searchViewModel.searchPlateFromText(context),
                )
                : null,
      ),
      drawer: Drawer(
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [_drawerHeader(context), _drawerMenuItems(context)],
          ),
        ),
      ),
      resizeToAvoidBottomInset: false,
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
        ),
      ),
    );
  }

  Widget _drawerHeader(BuildContext context) {
    return Container(
      color: AppColors.amber,
      padding: EdgeInsets.only(
        top: 24 + MediaQuery.of(context).padding.top,
        bottom: 24,
      ),
      child: Row(
        spacing: 8,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Image.asset("assets/images/app_icon.png", height: 32),
          Text(
            'ScannerFA',
            style: TextStyle(
              fontFamily: 'Italiana',
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }

  Widget _drawerMenuItems(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(24),
      child: Wrap(
        runSpacing: 8,
        children: [
          ListTile(
            leading: const Icon(Icons.home),
            title: const Text("Tela Inicial"),
            onTap: () {
              context.go('/home');
            },
          ),
          Visibility(
            visible: true,
            child: ListTile(
              leading: const Icon(Icons.admin_panel_settings),
              title: const Text("Admin"),
              onTap: () {},
            ),
          ),
          ListTile(
            leading: const Icon(Icons.settings_rounded),
            title: const Text("Configurações"),
            onTap: () {},
          ),
          Divider(color: Colors.black),
          ListTile(
            leading: const Icon(Icons.logout),
            title: const Text("Sair"),
            onTap: () {
              context.go('/');
            },
          ),
        ],
      ),
    );
  }

  // @override
  // Widget build(BuildContext context) {
  //   return Scaffold(
  //     appBar: AppBar(
  //       actions: [
  //         IconButton(
  //           onPressed: () {
  //             Navigator.pushReplacementNamed(context, "login");
  //           },
  //           icon: Icon(Icons.exit_to_app_outlined),
  //         ),
  //       ],
  //       backgroundColor: Colors.yellow.shade700,
  //       title: Text('Home Screen'),
  //       centerTitle: true,
  //     ),
  //     body: Ink(
  //       decoration: BoxDecoration(
  //         gradient: LinearGradient(
  //           colors: [Colors.indigo.shade900, AppColors.bgColor],
  //           begin: Alignment.topCenter,
  //           end: Alignment.bottomCenter,
  //         ),
  //       ),
  //       child: Container(
  //         margin: const EdgeInsets.all(16),
  //         decoration: BoxDecoration(
  //           color: Colors.white,
  //           borderRadius: BorderRadius.circular(20),
  //         ),
  //         child: Stack(
  //           children: [
  //             Padding(
  //               padding: const EdgeInsets.all(16.0),
  //               child: Column(
  //                 mainAxisAlignment: MainAxisAlignment.center,
  //                 crossAxisAlignment: CrossAxisAlignment.center,
  //                 children: [
  //                   SizedBox(height: 32, width: double.infinity),
  //                   Column(
  //                     spacing: 8,
  //                     children: [
  //                       Text(
  //                         "Tire uma foto",
  //                         style: TextStyle(
  //                           fontSize: 20,
  //                           fontWeight: FontWeight.bold,
  //                         ),
  //                       ),
  //                       SizedBox(
  //                         height: 128,
  //                         width: 128,
  //                         child: IconButton(
  //                           onPressed: () {
  //                             _handleCameraClicked(context);
  //                           },
  //                           icon: Icon(Icons.image_outlined, size: 96),
  //                           padding: EdgeInsets.zero,
  //                           style: ButtonStyle(
  //                             backgroundColor: WidgetStateColor.resolveWith((
  //                               states,
  //                             ) {
  //                               return Colors.yellow.shade700;
  //                             }),
  //                             shape: WidgetStateOutlinedBorder.resolveWith((
  //                               states,
  //                             ) {
  //                               return RoundedRectangleBorder(
  //                                 borderRadius: BorderRadius.circular(16),
  //                               );
  //                             }),
  //                           ),
  //                         ),
  //                       ),
  //                       SizedBox(height: 32),
  //                     ],
  //                   ),
  //                   SizedBox(height: 32),
  //                   SizedBox(
  //                     height: 32,
  //                     child: Stack(
  //                       children: [
  //                         Center(
  //                           child: SizedBox(
  //                             height: 2,
  //                             width: double.infinity,
  //                             child: ColoredBox(color: Colors.black),
  //                           ),
  //                         ),
  //                         Center(
  //                           child: Container(
  //                             alignment: Alignment.center,
  //                             width: 60,
  //                             padding: EdgeInsets.symmetric(horizontal: 16),
  //                             color: Colors.white,
  //                             child: Center(
  //                               child: Text(
  //                                 "OU",
  //                                 style: TextStyle(
  //                                   fontSize: 20,
  //                                   fontWeight: FontWeight.bold,
  //                                 ),
  //                               ),
  //                             ),
  //                           ),
  //                         ),
  //                       ],
  //                     ),
  //                   ),
  //                   SizedBox(height: 32),
  //                   Column(
  //                     spacing: 8,
  //                     children: [
  //                       Text(
  //                         "Digite a placa",
  //                         style: TextStyle(
  //                           fontSize: 20,
  //                           fontWeight: FontWeight.bold,
  //                         ),
  //                       ),
  //                       TextFormField(
  //                         textCapitalization: TextCapitalization.characters,
  //                         controller: textController,
  //                         decoration: InputDecoration(
  //                           filled: true,
  //                           fillColor: Colors.grey.shade300,
  //                           suffixIconConstraints: BoxConstraints.tight(
  //                             Size(50, 48),
  //                           ),
  //                           suffixIcon: Row(
  //                             children: [
  //                               SizedBox(
  //                                 width: 2,
  //                                 height: double.infinity,
  //                                 child: Container(color: Colors.black),
  //                               ),
  //                               IconButton(
  //                                 padding: EdgeInsets.zero,
  //                                 onPressed: () {
  //                                   _handleTextPlateSent(
  //                                     context,
  //                                     textController.text,
  //                                   );
  //                                 },
  //                                 icon: Icon(Icons.send),
  //                                 style: ButtonStyle(
  //                                   shape:
  //                                       WidgetStateOutlinedBorder.resolveWith((
  //                                         states,
  //                                       ) {
  //                                         return BeveledRectangleBorder();
  //                                       }),
  //                                 ),
  //                               ),
  //                             ],
  //                           ),
  //                           border: OutlineInputBorder(
  //                             borderSide: BorderSide.none,
  //                           ),
  //                         ),
  //                       ),
  //                       SizedBox(height: 32),
  //                     ],
  //                   ),
  //                 ],
  //               ),
  //             ),
  //             HelpWidget(),
  //           ],
  //         ),
  //       ),
  //     ),
  //   );
  // }

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
