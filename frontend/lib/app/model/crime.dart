// ignore_for_file: public_member_api_docs, sort_constructors_first
import 'dart:convert';

import 'package:frontend/app/model/person_record.dart';

class Crime {
  final int? id;
  final String crimeType;
  final DateTime crimeDateTime;
  final String description;
  final PersonRecord? personRecord;

  Crime({
    this.id,
    required this.crimeType,
    required this.crimeDateTime,
    required this.description,
    this.personRecord,
  });

  Crime copyWith({
    int? id,
    String? crimeType,
    DateTime? crimeDateTime,
    String? description,
    PersonRecord? personRecord,
  }) {
    return Crime(
      id: id ?? this.id,
      crimeType: crimeType ?? this.crimeType,
      crimeDateTime: crimeDateTime ?? this.crimeDateTime,
      description: description ?? this.description,
      personRecord: personRecord ?? this.personRecord,
    );
  }

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'id': id,
      'crimeType': crimeType,
      'crimeDateTime': crimeDateTime.millisecondsSinceEpoch,
      'description': description,
      'personRecord': personRecord?.toMap(),
    };
  }

  factory Crime.fromMap(Map<String, dynamic> map) {
    return Crime(
      id: map['id'] != null ? map['id'] as int : null,
      crimeType: map['crimeType'] as String,
      crimeDateTime: DateTime.fromMillisecondsSinceEpoch(
        map['crimeDateTime'] as int,
      ),
      description: map['description'] as String,
      personRecord:
          map['personRecord'] != null
              ? PersonRecord.fromMap(
                map['personRecord'] as Map<String, dynamic>,
              )
              : null,
    );
  }

  String toJson() => json.encode(toMap());

  factory Crime.fromJson(String source) =>
      Crime.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() {
    return 'Crime(id: $id, crimeType: $crimeType, crimeDateTime: $crimeDateTime, description: $description, personRecord: $personRecord)';
  }

  @override
  bool operator ==(covariant Crime other) {
    if (identical(this, other)) return true;

    return other.id == id &&
        other.crimeType == crimeType &&
        other.crimeDateTime == crimeDateTime &&
        other.description == description &&
        other.personRecord == personRecord;
  }

  @override
  int get hashCode {
    return id.hashCode ^
        crimeType.hashCode ^
        crimeDateTime.hashCode ^
        description.hashCode ^
        personRecord.hashCode;
  }
}
