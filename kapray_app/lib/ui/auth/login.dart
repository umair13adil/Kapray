import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:kapray_app/constants/images_const.dart';

class LoginScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _LoginScreenState();
  }
}

class _LoginScreenState extends State<LoginScreen> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            Image.asset(
              ImagesRef.SPLASH_IMG,
            )
          ],
        ),
      ),
    );
  }
}
