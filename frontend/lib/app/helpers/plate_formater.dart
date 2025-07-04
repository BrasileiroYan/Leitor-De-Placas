class PlateFormater {
  static String formatPlate(
    String plate, {
    bool dotMode = false,
    bool withSpacing = false,
  }) {
    if (plate.length < 4) return plate;
    final numbers = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];
    if (numbers.contains(plate[4]) &&
        ![plate[3], plate[5], plate[6]].any((e) => numbers.contains(e))) {
      plate =
          '${plate.substring(0, 3)}${dotMode ? '•' : '-'}${plate.substring(3, 7)}';
    }
    if (withSpacing) plate = plate.split('').join(' ');
    return plate;
  }
}
