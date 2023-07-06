import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class BatteryLevel extends StatefulWidget {
  const BatteryLevel({super.key});

  @override
  State<BatteryLevel> createState() => _BatteryLevelState();
}

class _BatteryLevelState extends State<BatteryLevel> {
  String _batteryLevel = "Unknown battery level";
  String _name = "Whoever";

  static const batteryChannelPlatform = MethodChannel('batteryLevel');
  static const nameChannelPlatform = MethodChannel('name');


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Method Channel Demo"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(38.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Column(
              children: [
                Text(_batteryLevel),
                const SizedBox(height: 10.0),
                ElevatedButton(
                    onPressed: _fetchBatteryLevel,
                    child: const Text("Fetch Battery level")),
              ],
            ),
            Column(
              children: [
                Text(_name),
                const SizedBox(height: 10.0),
                ElevatedButton(
                    onPressed: _fetchName, child: const Text("Fetch Name")),
              ],
            ),
          ],
        ),
      ),
    );
  }

  //Method to fetch battery level
  Future<void> _fetchBatteryLevel() async {
    String batteryLevel;
    try {
      final int result = await batteryChannelPlatform
          .invokeMethod('getBatteryLevel'); //Will fetch value from native code
      batteryLevel = 'Battery level at $result % .';
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }
    setState(() {
      _batteryLevel = batteryLevel; //set fetched value into declared variable
    });
  }

  //Method to fetch name
  Future<void> _fetchName() async {
    String name;
    try{
      final String result = await nameChannelPlatform.invokeMethod('getName');
      name = "Name is $result";
    } on PlatformException catch (e){
      name = "Failed to fetch name : ${e.message}.";
    }
    setState(() {
      _name = name;
    });
  }
}
