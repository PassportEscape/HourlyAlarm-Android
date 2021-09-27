package com.pasaporteescaperoom.hourlyalarm

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.Toast
import com.pasaporteescaperoom.hourlyalarm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator


class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    val  alarm = Alarm();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alarm.showNotification(applicationContext);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoreForm()
        start()
    }

    fun restoreForm() {
        enableHourlySwitch.setText("TEST")
        enableHourlySwitch.setOnClickListener {
            enableHourlyClick(enableHourlySwitch);
        }
        snedNotification.setOnClickListener{
            alarm.showNotification(applicationContext)
        }
    }


    fun enableHourlyClick(view: Switch) {

        Log.i("SWITCH", "Toggle")
        Log.i("SWITCH STATUS", view.isChecked.toString())
        if (view.isChecked) {
            start();
        } else {
            stop()
        }
    }

    private fun start() {
        /*val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val mVibratePattern = longArrayOf(0, 400, 800, 600, 800, 800, 800, 1000)
        val mAmplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255)

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            vibrator.vibrate(500)
        }*/

//        Toast.makeText(applicationContext, "Enabled!", Toast.LENGTH_SHORT).show()
        //val i = Intent("com.pasaporteescaperoom.hourlyalarm.START_ALARM")
        alarm.setAlarm(applicationContext)
    }

    private fun stop() {
        //Toast.makeText(applicationContext, "Disabled :(", Toast.LENGTH_SHORT).show()
        alarm.cancelAlarm(applicationContext)
    }
}