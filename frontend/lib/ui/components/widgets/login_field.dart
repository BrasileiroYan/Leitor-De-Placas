import 'package:flutter/material.dart';

class LoginField extends StatefulWidget {
  final TextEditingController controller;
  final String? hintText;
  final bool obscureText;
  const LoginField({
    super.key,
    required this.controller,
    this.obscureText = false,
    this.hintText,
  });

  @override
  State<LoginField> createState() => _LoginFieldState();
}

class _LoginFieldState extends State<LoginField> {
  final FocusNode _focusNode = FocusNode();
  bool _isObscured = true;

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: widget.controller,
      focusNode: _focusNode,
      onEditingComplete: () {
        _focusNode.unfocus();
      },
      cursorColor: Colors.black,
      obscureText: widget.obscureText ? _isObscured : false,
      enableSuggestions: !widget.obscureText,
      style: TextStyle(decorationColor: Colors.white),
      decoration: InputDecoration(
        filled: true,
        fillColor: Colors.white,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.all(Radius.circular(8)),
        ),
        hintText: widget.hintText,
        suffixIcon: Visibility(
          visible: widget.obscureText,
          child: IconButton(
            onPressed: _onVisibiltyPressed,
            icon:
                _isObscured
                    ? Icon(Icons.visibility)
                    : Icon(Icons.visibility_off),
          ),
        ),
      ),
    );
  }

  _onVisibiltyPressed() {
    setState(() {
      _isObscured = !_isObscured;
    });
  }
}
