import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:kapray_app/constants/colors_const.dart';
import 'package:kapray_app/constants/images_const.dart';
import 'package:kapray_app/ui/auth/login.dart';

class SplashScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _SplashScreenState();
  }
}

class _SplashScreenState extends State<SplashScreen>
    with SingleTickerProviderStateMixin {
  AnimationController _animationController;
  Animation<double> _animation;

  @override
  void initState() {
    super.initState();

    _animationController = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 5000));

    void handler(status) {
      if (status == AnimationStatus.completed) {
        Navigator.push(
          context,
          PageRouteBuilder(
            pageBuilder: (context, _, __) => LoginScreen(),
            transitionsBuilder:
                (context, animation, secondaryAnimation, child) =>
                    FadeTransition(
                      opacity: animation,
                      child: child,
                    ),
          ),
        );
      }
    }

    _animation = CurvedAnimation(
      parent: _animationController,
      curve: Curves.easeIn,
    )..addStatusListener(handler);

    //this will start the animation
    _animationController.forward();
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
    ));

    return Scaffold(
      backgroundColor: AppColors.colorPrimary,
      body: FadeTransition(
        opacity: _animation,
        child: Center(
          child: Image.asset(
            ImagesRef.APP_ICON_TRANS,
            fit: BoxFit.scaleDown,
            scale: 0.5,
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }
}
