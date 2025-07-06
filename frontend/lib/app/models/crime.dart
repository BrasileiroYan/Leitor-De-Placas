// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

class Crime {
  String crimeType;
  String crimeDateTime;
  String description;
  String crimeStatus;

  Crime({
    required this.crimeType,
    required this.crimeDateTime,
    required this.description,
    required this.crimeStatus,
  });

  Crime copyWith({
    String? crimeType,
    String? crimeDateTime,
    String? description,
    String? crimeStatus,
  }) {
    return Crime(
      crimeType: crimeType ?? this.crimeType,
      crimeDateTime: crimeDateTime ?? this.crimeDateTime,
      description: description ?? this.description,
      crimeStatus: crimeStatus ?? this.crimeStatus,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'crimeType': crimeType,
      'crimeDateTime': crimeDateTime,
      'description': description,
      'crimeStatus': crimeStatus,
    };
  }

  factory Crime.fromMap(Map<String, dynamic> map) {
    return Crime(
      crimeType: map['crimeType'] as String,
      crimeDateTime: map['crimeDateTime'] as String,
      description: map['description'] as String,
      crimeStatus: map['crimeStatus'] as String,
    );
  }

  String toJson() => json.encode(toMap());

  factory Crime.fromJson(String source) =>
      Crime.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() {
    return 'Crime(crimeType: $crimeType, crimeDateTime: $crimeDateTime, description: $description, crimeStatus: $crimeStatus)';
  }

  @override
  bool operator ==(covariant Crime other) {
    if (identical(this, other)) return true;

    return other.crimeType == crimeType &&
        other.crimeDateTime == crimeDateTime &&
        other.description == description &&
        other.crimeStatus == crimeStatus;
  }

  @override
  int get hashCode {
    return crimeType.hashCode ^
        crimeDateTime.hashCode ^
        description.hashCode ^
        crimeStatus.hashCode;
  }

  factory Crime.getExample1() => Crime.fromMap({
    "crimeType": "Furto",
    "crimeDateTime": "10/01/2023 - 10:30:00",
    "description": "Furto de veículo",
    "crimeStatus": "CONDENADO",
  });

  factory Crime.getExample2() => Crime.fromMap({
    "crimeType": "Fraude",
    "crimeDateTime": "21/10/2019 - 07:30:00",
    "description": "Fraude bancária",
    "crimeStatus": "SOB_INVESTIGACAO",
  });
}
