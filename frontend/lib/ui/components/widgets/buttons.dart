import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:frontend/app/viewmodels/camera_viewmodel.dart';

class PrimaryButton extends StatelessWidget {
  final String text;
  final IconData? icon;
  final Function onTap;
  final Color? bgColor;

  const PrimaryButton({
    super.key,
    required this.text,
    this.icon,
    required this.onTap,
    this.bgColor,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      borderRadius: BorderRadius.circular(16),
      onTap: () {
        onTap();
      },
      child: Ink(
        padding: EdgeInsets.symmetric(vertical: 8, horizontal: 32),
        decoration: BoxDecoration(
          color: bgColor ?? Colors.green,
          borderRadius: BorderRadius.circular(16),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              text,
              style: TextStyle(
                color: Colors.white,
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
            icon != null
                ? Row(
                  children: [
                    SizedBox(width: 10),
                    Icon(icon, color: Color(0xFF1D0E44)),
                  ],
                )
                : Container(),
          ],
        ),
      ),
    );
  }
}

class FlashCycleButton extends StatefulWidget {
  final CameraController controller;

  const FlashCycleButton({super.key, required this.controller});

  @override
  State<FlashCycleButton> createState() => _FlashCycleButtonState();
}

class _FlashCycleButtonState extends State<FlashCycleButton> {
  late FlashMode _currentMode;

  @override
  void initState() {
    super.initState();
    _currentMode = widget.controller.value.flashMode;
  }

  void _cycleFlash() async {
    FlashMode nextMode;
    switch (_currentMode) {
      case FlashMode.off:
        nextMode = FlashMode.auto;
        break;
      case FlashMode.auto:
        nextMode = FlashMode.always;
        break;
      case FlashMode.always:
        nextMode = FlashMode.torch;
        break;
      case FlashMode.torch:
        nextMode = FlashMode.off;
        break;
    }

    await widget.controller.setFlashMode(nextMode);

    setState(() {
      _currentMode = nextMode;
    });
  }

  IconData _iconForMode(FlashMode mode) {
    switch (mode) {
      case FlashMode.off:
        return Icons.flash_off;
      case FlashMode.auto:
        return Icons.flash_auto;
      case FlashMode.always:
        return Icons.flash_on;
      case FlashMode.torch:
        return Icons.highlight; // torch icon
    }
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      borderRadius: BorderRadius.circular(20),
      onTap: _cycleFlash,
      child: Ink(
        padding: EdgeInsets.all(4),
        child: Icon(_iconForMode(_currentMode), color: Colors.white, size: 20),
      ),
    );
  }
}

class AspectRatioCycleButton extends StatefulWidget {
  final CameraViewModel viewModel;
  const AspectRatioCycleButton({super.key, required this.viewModel});

  @override
  State<AspectRatioCycleButton> createState() => _AspectRatioCycleButtonState();
}

class _AspectRatioCycleButtonState extends State<AspectRatioCycleButton> {
  late AspectRatios _currentMode;

  @override
  void initState() {
    super.initState();
    _currentMode = widget.viewModel.aspectRatio;
  }

  void _cycleRatio() async {
    AspectRatios nextMode;
    switch (_currentMode) {
      case AspectRatios.threeFour:
        nextMode = AspectRatios.nineSixteen;
        break;
      case AspectRatios.nineSixteen:
        nextMode = AspectRatios.oneOne;
        break;
      case AspectRatios.oneOne:
        nextMode = AspectRatios.threeFour;
        break;
    }

    _currentMode = nextMode;
    await widget.viewModel.setAspectRatio(nextMode);
  }

  String _textForMode(AspectRatios mode) {
    switch (mode) {
      case AspectRatios.threeFour:
        return '3:4';
      case AspectRatios.nineSixteen:
        return '9:16';
      case AspectRatios.oneOne:
        return '1:1';
    }
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      borderRadius: BorderRadius.circular(8),
      onTap: _cycleRatio,
      child: Ink(
        padding: EdgeInsets.all(4),
        child: Text(
          _textForMode(_currentMode),
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
    );
  }
}
