import 'package:flutter/material.dart';
import 'package:frontend/app/models/crime.dart';

class CrimesListSection extends StatelessWidget {
  final List<Crime> crimes;

  const CrimesListSection({super.key, required this.crimes});

  @override
  Widget build(BuildContext context) {
    if (crimes.isEmpty) {
      return const Padding(
        padding: EdgeInsets.only(top: 8),
        child: Text(
          'No crimes found.',
          style: TextStyle(fontSize: 16, color: Colors.grey),
        ),
      );
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Criminal Record',
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        const SizedBox(height: 8),
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Row(
            children: [
              ...crimes.map(
                (crime) => Card(
                  color: Colors.red.shade50,
                  elevation: 2,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          crime.crimeType,
                          style: TextStyle(fontWeight: FontWeight.bold),
                        ),
                        // CrimeInfoRow('Type', crime.crimeType),
                        CrimeInfoRow('Date', crime.crimeDateTime),
                        CrimeInfoRow('Description', crime.description),
                        CrimeInfoRow('Status', crime.crimeStatus),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget CrimeInfoRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 2),
      child: Text('$label: $value', style: const TextStyle(fontSize: 15)),
    );
  }
}
