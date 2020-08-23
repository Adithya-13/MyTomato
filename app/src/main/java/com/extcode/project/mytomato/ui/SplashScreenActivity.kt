package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPrefName =
            getSharedPreferences(NameActivity::class.simpleName, Context.MODE_PRIVATE)
        val sharedPrefSchedules = getSharedPreferences("isScheduleStarted", Context.MODE_PRIVATE)

        when {
            sharedPrefName.all.isNullOrEmpty() -> intent(NameActivity::class.java)
            sharedPrefSchedules.getBoolean(
                "isStarted",
                true
            ) -> intent(GetSchedulesActivity::class.java)
            else -> intent(MainActivity::class.java)
        }
    }

    private fun intent(cls: Class<*>) {

        Handler().postDelayed({
            startActivity(Intent(this, cls))
            finish()
        }, 1000L)

    }

}