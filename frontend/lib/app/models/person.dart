// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:flutter/foundation.dart';

import 'package:frontend/app/models/address.dart';
import 'package:frontend/app/models/crime.dart';

class Person {
  String fullName;
  String rg;
  String cpf;
  String birthDate;
  String genero;
  String licenseCategory;
  Address address;
  List<Crime> crimesList;

  Person({
    required this.fullName,
    required this.rg,
    required this.cpf,
    required this.birthDate,
    required this.genero,
    required this.licenseCategory,
    required this.address,
    required this.crimesList,
  });

  Person copyWith({
    String? fullName,
    String? rg,
    String? cpf,
    String? birthDate,
    String? genero,
    String? licenseCategory,
    Address? address,
    List<Crime>? crimesList,
  }) {
    return Person(
      fullName: fullName ?? this.fullName,
      rg: rg ?? this.rg,
      cpf: cpf ?? this.cpf,
      birthDate: birthDate ?? this.birthDate,
      genero: genero ?? this.genero,
      licenseCategory: licenseCategory ?? this.licenseCategory,
      address: address ?? this.address,
      crimesList: crimesList ?? this.crimesList,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'fullName': fullName,
      'rg': rg,
      'cpf': cpf,
      'birthDate': birthDate,
      'genero': genero,
      'licenseCategory': licenseCategory,
      'address': address.toMap(),
      'crimesList': crimesList.map((x) => x.toMap()).toList(),
    };
  }

  factory Person.fromMap(Map<String, dynamic> map) {
    return Person(
      fullName: map['fullName'] as String,
      rg: map['rg'] as String,
      cpf: map['cpf'] as String,
      birthDate: map['birthDate'] as String,
      genero: map['genero'] as String,
      licenseCategory: map['licenseCategory'] as String,
      address: Address.fromMap(map['address'] as Map<String, dynamic>),
      crimesList: List<Crime>.from(
        (map['crimesList'] as List<int>).map<Crime>(
          (x) => Crime.fromMap(x as Map<String, dynamic>),
        ),
      ),
    );
  }

  String toJson() => json.encode(toMap());

  factory Person.fromJson(String source) =>
      Person.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() {
    return 'Person(fullName: $fullName, rg: $rg, cpf: $cpf, birthDate: $birthDate, genero: $genero, licenseCategory: $licenseCategory, address: $address, crimesList: $crimesList)';
  }

  @override
  bool operator ==(covariant Person other) {
    if (identical(this, other)) return true;

    return other.fullName == fullName &&
        other.rg == rg &&
        other.cpf == cpf &&
        other.birthDate == birthDate &&
        other.genero == genero &&
        other.licenseCategory == licenseCategory &&
        other.address == address &&
        listEquals(other.crimesList, crimesList);
  }

  @override
  int get hashCode {
    return fullName.hashCode ^
        rg.hashCode ^
        cpf.hashCode ^
        birthDate.hashCode ^
        genero.hashCode ^
        licenseCategory.hashCode ^
        address.hashCode ^
        crimesList.hashCode;
  }

  factory Person.getExample() => Person.fromMap({
    "fullName": "João Silva",
    "rg": "12345678",
    "cpf": "123.456.789-00",
    "birthDate": "15/01/1990",
    "genero": "Masculino",
    "licenseCategory": "AB",
    "criminalRecord": {"crimesList": <Crime>[]},
  });
}
