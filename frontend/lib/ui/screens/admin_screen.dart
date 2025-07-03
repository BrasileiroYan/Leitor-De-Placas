import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/admin_viewmodel.dart';
import 'package:frontend/ui/components/_core/app_colors.dart';
import 'package:frontend/ui/components/_core/app_background_gradient.dart';
import 'package:frontend/ui/components/widgets/dialogs/help_dialog.dart';
import 'package:frontend/ui/components/widgets/modals/add_user_modal.dart';
import 'package:frontend/ui/components/widgets/navigation_drawer_widget.dart';
import 'package:provider/provider.dart';

class AdminScreen extends StatelessWidget {
  const AdminScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final viewModel = Provider.of<AdminViewModel>(context);

    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(
            icon: Icon(viewModel.isSearching ? Icons.close : Icons.search),
            onPressed: () {
              viewModel.setSearching(!viewModel.isSearching);
              if (!viewModel.isSearching) {
                viewModel.searchController.text = '';
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
        title:
            viewModel.isSearching
                ? TextField(
                  controller: viewModel.searchController,
                  autofocus: true,
                  decoration: InputDecoration(
                    hintText:
                        'Pesquisar ${viewModel.isSearchingId ? 'id' : 'nome de usuário'}...',
                  ),
                  style: const TextStyle(color: Colors.white),
                  onChanged: (query) {
                    viewModel.searchUsers(query);
                  },
                  // onSubmitted: (value) async {},
                )
                : null,
      ),
      drawer: NavigationDrawerWidget(),
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(right: 16, bottom: 16),
        child: FloatingActionButton(
          onPressed: () {
            showModalBottomSheet(
              showDragHandle: true,
              backgroundColor: Colors.grey.shade300,
              context: context,
              isScrollControlled: true,
              builder: (context) => const AddUserModal(),
            );
          },
          backgroundColor: AppColors.amber,
          child: const Icon(Icons.add, color: Colors.black),
        ),
      ),
      resizeToAvoidBottomInset: false,
      body: AppBackgroundGradient(
        child: Stack(
          children: [
            Container(
              margin: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(20),
              ),
              child:
                  !viewModel.isLoading
                      ? RefreshIndicator(
                        onRefresh: () => viewModel.refreshUsersList(),
                        child: ListView(
                          padding: EdgeInsets.symmetric(horizontal: 8),
                          // shrinkWrap: true,
                          children: List.generate(
                            viewModel.filteredList.length,
                            (index) {
                              final user = viewModel.filteredList.elementAt(
                                index,
                              );
                              return ListTile(
                                leading: CircleAvatar(),
                                title: Text(user.username),
                              );
                            },
                          ),
                        ),
                      )
                      : Center(child: CircularProgressIndicator()),
            ),
          ],
        ),
      ),
    );
  }
}
