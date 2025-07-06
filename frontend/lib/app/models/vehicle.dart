// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:frontend/app/models/person.dart';

class Vehicle {
  String plate;
  String vehicleType;
  String brand;
  String model;
  String color;
  int fabricationYear;
  String ipvaStatus;
  Person owner;

  Vehicle({
    required this.plate,
    required this.vehicleType,
    required this.brand,
    required this.model,
    required this.color,
    required this.fabricationYear,
    required this.ipvaStatus,
    required this.owner,
  });

  Vehicle copyWith({
    String? plate,
    String? vehicleType,
    String? brand,
    String? model,
    String? color,
    int? fabricationYear,
    String? ipvaStatus,
    Person? owner,
  }) {
    return Vehicle(
      plate: plate ?? this.plate,
      vehicleType: vehicleType ?? this.vehicleType,
      brand: brand ?? this.brand,
      model: model ?? this.model,
      color: color ?? this.color,
      fabricationYear: fabricationYear ?? this.fabricationYear,
      ipvaStatus: ipvaStatus ?? this.ipvaStatus,
      owner: owner ?? this.owner,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'plate': plate,
      'vehicleType': vehicleType,
      'brand': brand,
      'model': model,
      'color': color,
      'fabricationYear': fabricationYear,
      'ipvaStatus': ipvaStatus,
      'owner': owner.toMap(),
    };
  }

  factory Vehicle.fromMap(Map<String, dynamic> map) {
    return Vehicle(
      plate: map['plate'] as String,
      vehicleType: map['vehicleType'] as String,
      brand: map['brand'] as String,
      model: map['model'] as String,
      color: map['color'] as String,
      fabricationYear: map['fabricationYear'] as int,
      ipvaStatus: map['ipvaStatus'] as String,
      owner: Person.fromMap(map['owner'] as Map<String, dynamic>),
    );
  }

  String toJson() => json.encode(toMap());

  factory Vehicle.fromJson(String source) =>
      Vehicle.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() {
    return 'Vehicle(plate: $plate, vehicleType: $vehicleType, brand: $brand, model: $model, color: $color, fabricationYear: $fabricationYear, ipvaStatus: $ipvaStatus, owner: $owner)';
  }

  @override
  bool operator ==(covariant Vehicle other) {
    if (identical(this, other)) return true;

    return other.plate == plate &&
        other.vehicleType == vehicleType &&
        other.brand == brand &&
        other.model == model &&
        other.color == color &&
        other.fabricationYear == fabricationYear &&
        other.ipvaStatus == ipvaStatus &&
        other.owner == owner;
  }

  @override
  int get hashCode {
    return plate.hashCode ^
        vehicleType.hashCode ^
        brand.hashCode ^
        model.hashCode ^
        color.hashCode ^
        fabricationYear.hashCode ^
        ipvaStatus.hashCode ^
        owner.hashCode;
  }

  factory Vehicle.getExample() => Vehicle(
    plate: "ABC1234",
    vehicleType: "CARRO",
    brand: "Honda",
    model: "Civic",
    color: "Preto",
    fabricationYear: 2018,
    ipvaStatus: "PAGO",
    owner: Person.getExample(),
  );
}
