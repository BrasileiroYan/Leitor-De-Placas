import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/admin_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

class AddUserModal extends StatefulWidget {
  const AddUserModal({super.key});

  @override
  State<AddUserModal> createState() => _AddUserModalState();
}

class _AddUserModalState extends State<AddUserModal> {
  final TextEditingController _nameController = TextEditingController();
  // AppUserRole _accountRole = AppUserRole.standard;

  bool isLoading = false;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width - 24,
      // height: MediaQuery.of(context).size.height * 0.65,
      padding: EdgeInsets.fromLTRB(
        16,
        0,
        16,
        MediaQuery.of(context).viewInsets.bottom,
      ),
      child: SingleChildScrollView(
        child: Column(
          spacing: 8,
          mainAxisSize: MainAxisSize.min,
          children: [
            // const SizedBox(height: 24),
            Column(
              spacing: 8,
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Center(
                  child: Image.asset(
                    "assets/images/icon_add_account.png",
                    width: 64,
                  ),
                ),
                const Text(
                  "Adicionar novo usuário",
                  style: TextStyle(fontWeight: FontWeight.w400, fontSize: 24),
                ),
                const Text(
                  "Forneça o e-mail: ",
                  style: TextStyle(fontWeight: FontWeight.w400, fontSize: 14),
                ),
              ],
            ),
            // const SizedBox(height: 8),
            Column(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              spacing: 32,
              children: [
                TextFormField(
                  controller: _nameController,
                  decoration: InputDecoration(label: Text("E-mail")),
                ),
                // Column(
                //   crossAxisAlignment: CrossAxisAlignment.start,
                //   children: [
                //     Text("Tipo de conta: "),
                //     DropdownButton<AppUserRole>(
                //       value: _accountRole,
                //       isExpanded: true,
                //       items: const [
                //         DropdownMenuItem(
                //           value: AppUserRole.standard,
                //           child: Text("Padrão"),
                //         ),
                //         DropdownMenuItem(
                //           value: AppUserRole.admin,
                //           child: Text("Administrador"),
                //         ),
                //       ],
                //       onChanged: (value) {
                //         if (value != null) {
                //           setState(() {
                //             _accountRole = value;
                //           });
                //         }
                //       },
                //     ),
                //   ],
                // ),
                Row(
                  spacing: 8,
                  children: [
                    Expanded(
                      child: ElevatedButton(
                        onPressed:
                            (isLoading)
                                ? null
                                : () {
                                  onCancelButtonClicked();
                                },
                        style: ButtonStyle(
                          backgroundColor: WidgetStatePropertyAll(Colors.white),
                        ),
                        child: Text(
                          "Cancelar",
                          style: TextStyle(color: Colors.black),
                        ),
                      ),
                    ),
                    Expanded(
                      child: ElevatedButton(
                        style: ButtonStyle(
                          backgroundColor: WidgetStatePropertyAll(
                            AppColors.lightBgColor,
                          ),
                        ),
                        onPressed: () {
                          onAddButtonClicked();
                        },
                        child:
                            (isLoading)
                                ? const SizedBox(
                                  width: 16,
                                  height: 16,
                                  child: CircularProgressIndicator(
                                    color: Colors.white,
                                  ),
                                )
                                : const Text(
                                  "Adicionar",
                                  style: TextStyle(color: Colors.white),
                                ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
            const SizedBox(height: 8),
          ],
        ),
      ),
    );
  }

  onCancelButtonClicked() {
    if (isLoading) return;
    closeModal();
  }

  onAddButtonClicked() async {
    if (isLoading) return;
    String username = _nameController.text.trim();

    debugPrint("\t$username");

    await context.read<AdminViewModel>().addUser(context, username);
  }

  closeModal() {
    context.pop();
  }
}
