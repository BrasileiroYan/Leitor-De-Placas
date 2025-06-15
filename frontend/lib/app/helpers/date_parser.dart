DateTime parseDate(String dateStr) {
  final parts = dateStr.split('/');
  if (parts.length != 3) {
    throw FormatException('Invalid date format: $dateStr');
  }
  final day = int.parse(parts[0]);
  final month = int.parse(parts[1]);
  final year = int.parse(parts[2]);
  return DateTime(year, month, day);
}

DateTime parseDateTime(String dateStr) {
  final parts = dateStr.split(' - ');
  if (parts.length != 2) {
    throw FormatException('Invalid date format: $dateStr');
  }

  final dateParts = parts[0].split('/');
  final timeParts = parts[1].split(':');

  final day = int.parse(dateParts[0]);
  final month = int.parse(dateParts[1]);
  final year = int.parse(dateParts[2]);

  final hour = int.parse(timeParts[0]);
  final minute = int.parse(timeParts[1]);
  final second = int.parse(timeParts[2]);

  return DateTime(year, month, day, hour, minute, second);
}
