// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

enum AppUserRole { admin, standard }

class AppUser {
  int id;
  String username;
  AppUserRole role;

  AppUser({required this.id, required this.username, required this.role});

  AppUser copyWith({int? id, String? username, AppUserRole? role}) {
    return AppUser(
      id: id ?? this.id,
      username: username ?? this.username,
      role: role ?? this.role,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'id': id,
      'username': username,
      'role': role.name.toUpperCase(),
    };
  }

  factory AppUser.fromMap(Map<String, dynamic> map) {
    return AppUser(
      id: map['id'] as int,
      username: map['username'] as String,
      role: AppUserRole.values.firstWhere(
        (e) => (map['role'] as String).toLowerCase() == e.name,
      ),
    );
  }

  String toJson() => json.encode(toMap());

  factory AppUser.fromJson(String source) =>
      AppUser.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() => 'AppUser(id: $id, username: $username, role: $role)';

  @override
  bool operator ==(covariant AppUser other) {
    if (identical(this, other)) return true;

    return other.id == id && other.username == username && other.role == role;
  }

  @override
  int get hashCode => id.hashCode ^ username.hashCode ^ role.hashCode;
}
