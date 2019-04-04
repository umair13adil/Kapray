
import 'package:flutter/material.dart';
import 'package:kapray_app/ui/auth/login.dart';
import 'package:kapray_app/ui/splash/splash.dart';
import 'constants/routes_const.dart' as rc;

class Routes {
  static final routes = <String, WidgetBuilder>{
    rc.Routes.splash: (BuildContext context) => SplashScreen(),
    rc.Routes.login: (BuildContext context) => LoginScreen(),
  };
}
