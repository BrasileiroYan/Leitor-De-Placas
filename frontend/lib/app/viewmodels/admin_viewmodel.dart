import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/models/app_user.dart';
import 'package:frontend/app/services/admin_service.dart';
import 'package:frontend/app/viewmodels/search_viewmodel.dart';
import 'package:get_it/get_it.dart';
import 'package:go_router/go_router.dart';

class AdminViewModel extends SearchViewModel {
  List<AppUser> _usersList = [];
  List<AppUser> _filteredList = [];

  bool _isUserAdmin = false;
  bool _isSearchingId = false;

  bool get isUserAdmin => _isUserAdmin;
  bool get isSearchingId => _isSearchingId;
  List<AppUser> get usersList => _usersList;
  List<AppUser> get filteredList => _filteredList;

  Future<bool> getPermissionLevel() async {
    final adminService = GetIt.instance<AdminService>();
    _isUserAdmin = await adminService.getUserPermissionLevel();
    return _isUserAdmin;
  }

  Future<List<AppUser>> getAllUsers() async {
    final adminService = GetIt.instance<AdminService>();

    _usersList = await adminService.getAllUsers();

    if (_filteredList.isEmpty) {
      _filteredList = _usersList;
    }

    return _usersList;
  }

  Future<void> addUser(BuildContext context, String username) async {
    final adminService = GetIt.instance<AdminService>();

    if (username.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Por favor, insira e-mail do novo usuário.')),
      );
      return;
    }

    setLoading(true);

    try {
      final success = await adminService.createAndActivateUser(username);
      // setLoading(false);

      if (success) {
        if (!context.mounted) return;
        context.pop();
      } else {
        if (!context.mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Adição falhou, tente novamente.')),
        );
      }
    } on DioException catch (e) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('${e.response!.data['message']}')));
      context.pop();
    } finally {
      setLoading(false);
    }
  }

  List<AppUser> searchUsers(String query) {
    if (query.isEmpty) {
      _filteredList = _usersList;
    } else {
      _filteredList =
          _usersList.where((e) {
            final atIndex = e.username.indexOf('@');
            final prefix =
                atIndex != -1 ? e.username.substring(0, atIndex) : e.username;
            return prefix.contains(query);
          }).toList();
    }
    notifyListeners();
    return _filteredList;
  }

  @override
  Future<List<String>> fetchSearchScope() async {
    final adminService = GetIt.instance<AdminService>();

    final usersList = await adminService.getAllUsers();
    searchScope = usersList.map((e) => e.username).toList();

    return searchScope;
  }
}
