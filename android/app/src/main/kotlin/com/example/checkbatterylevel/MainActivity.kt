package com.example.checkbatterylevel

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val batteryLevelChannel = "batteryLevel"
    private val nameChannel = "name"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        //Battery Level Channel
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            batteryLevelChannel
        ).setMethodCallHandler { call, result ->
            if (call.method == "getBatteryLevel") {
                val batteryLevel = getBatteryLevel()
                if (batteryLevel != -1) {
                    result.success(batteryLevel)
                } else {
                    result.error("UNAVAILABLE", "Battery level not available.", null)
                }
            } else {
                result.notImplemented()
            }
        }

        //Name Channel
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            nameChannel
        ).setMethodCallHandler { call, result ->
            if (call.method == "getName") {
                val name = getName()
                if (name.isNotEmpty()) {
                    result.success(name)
                } else {
                    result.error("UNAVAILABLE", "Name is not available", null)
                }
            } else {
                result.notImplemented()
            }
        }

    }

    //Function that fetches battery Level
    private fun getBatteryLevel(): Int {
        val batteryLevel: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(
                null, IntentFilter(
                    Intent.ACTION_BATTERY_CHANGED
                )
            )
            intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(
                BatteryManager.EXTRA_SCALE,
                -1
            )
        }

        return batteryLevel
    }

    //Function that fetches name
    private fun getName(): String {
        return "Pranav"
    }

}
