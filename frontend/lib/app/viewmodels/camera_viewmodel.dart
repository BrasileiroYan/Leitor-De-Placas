import 'package:flutter/material.dart';

enum AspectRatios { threeFour, nineSixteen, oneOne }

class CameraViewModel with ChangeNotifier {
  AspectRatios aspectRatio = AspectRatios.threeFour;

  double ratioValue = 3 / 4;

  setAspectRatio(AspectRatios nextMode) {
    aspectRatio = nextMode;
    ratioValue = switch (aspectRatio) {
      AspectRatios.threeFour => 3 / 4,
      AspectRatios.nineSixteen => 9 / 16,
      AspectRatios.oneOne => 1 / 1,
    };
    notifyListeners();
  }
}
