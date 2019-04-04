import 'package:flutter/material.dart';
import 'package:kapray_app/constants/colors_const.dart';
import 'package:kapray_app/constants/strings_const.dart';
import 'package:kapray_app/routes.dart';
import 'package:kapray_app/ui/splash/splash.dart';

Future main() async {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: Strings.appName,
      routes: Routes.routes,
      theme: ThemeData(
          primaryColor: AppColors.colorPrimary,
          primaryColorDark: AppColors.colorPrimaryDark,
          accentColor: AppColors.colorAccent,
          fontFamily: 'Raleway'),
      home: SplashScreen(),
    );
  }
}
