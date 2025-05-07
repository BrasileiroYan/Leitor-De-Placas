// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:frontend/model/crime.dart';

class PersonRecord {
  final int? id;
  final String plate;
  final String fullName;
  final String rg;
  final String cpf;
  final DateTime birthDate;
  final List<Crime> crimes;
  PersonRecord({
    this.id,
    required this.plate,
    required this.fullName,
    required this.rg,
    required this.cpf,
    required this.birthDate,
  }) : crimes = <Crime>[];

  PersonRecord copyWith({
    int? id,
    String? plate,
    String? fullName,
    String? rg,
    String? cpf,
    DateTime? birthDate,
  }) {
    return PersonRecord(
      id: id ?? this.id,
      plate: plate ?? this.plate,
      fullName: fullName ?? this.fullName,
      rg: rg ?? this.rg,
      cpf: cpf ?? this.cpf,
      birthDate: birthDate ?? this.birthDate,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'id': id,
      'plate': plate,
      'fullName': fullName,
      'rg': rg,
      'cpf': cpf,
      'birthDate': birthDate.millisecondsSinceEpoch,
    };
  }

  factory PersonRecord.fromMap(Map<String, dynamic> map) {
    return PersonRecord(
      id: map['id'] != null ? map['id'] as int : null,
      plate: map['plate'] as String,
      fullName: map['fullName'] as String,
      rg: map['rg'] as String,
      cpf: map['cpf'] as String,
      birthDate: DateTime.fromMillisecondsSinceEpoch(map['birthDate'] as int),
    );
  }

  String toJson() => json.encode(toMap());

  factory PersonRecord.fromJson(String source) =>
      PersonRecord.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() {
    return 'PersonRecord(id: $id, plate: $plate, fullName: $fullName, rg: $rg, cpf: $cpf, birthDate: $birthDate)';
  }

  @override
  bool operator ==(covariant PersonRecord other) {
    if (identical(this, other)) return true;

    return other.id == id &&
        other.plate == plate &&
        other.fullName == fullName &&
        other.rg == rg &&
        other.cpf == cpf &&
        other.birthDate == birthDate;
  }

  @override
  int get hashCode {
    return id.hashCode ^
        plate.hashCode ^
        fullName.hashCode ^
        rg.hashCode ^
        cpf.hashCode ^
        birthDate.hashCode;
  }
}
