package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.ui.InputSchedulesActivity.Companion.EXTRA_ARR_SCHEDULE
import com.extcode.project.mytomato.ui.InputSchedulesActivity.Companion.EXTRA_DAY_CODE
import com.extcode.project.mytomato.ui.InputSchedulesActivity.Companion.EXTRA_NAME_DAY
import com.extcode.project.mytomato.ui.SettingActivity.Companion.EXTRA_FROM_SETTING
import kotlinx.android.synthetic.main.layout_schedules.*


class GetSchedulesActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_schedules)

        getSharedPreferences(getString(R.string.isScheduleStarted), Context.MODE_PRIVATE).edit {
            putBoolean("isStarted", false)
            commit()
        }

        monday.setOnClickListener(this)
        tuesday.setOnClickListener(this)
        wednesday.setOnClickListener(this)
        thursday.setOnClickListener(this)
        friday.setOnClickListener(this)
        saturday.setOnClickListener(this)
        sunday.setOnClickListener(this)
        toMainActivity.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.monday -> inputSchedules(0, "Monday")
            R.id.tuesday -> inputSchedules(1, "Tuesday")
            R.id.wednesday -> inputSchedules(2, "Wednesday")
            R.id.thursday -> inputSchedules(3, "Thursday")
            R.id.friday -> inputSchedules(4, "Friday")
            R.id.saturday -> inputSchedules(5, "Saturday")
            R.id.sunday -> inputSchedules(6, "Sunday")
            R.id.toMainActivity -> toMainActivity()
        }
    }

    private fun toMainActivity() {
        if (intent.getBooleanExtra(EXTRA_FROM_SETTING, false)) {

            startActivity(Intent(this, SettingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
            finish()

        } else {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    private fun inputSchedules(dayCode: Int, nameDay: String) {
        startActivityForResult(Intent(this, InputSchedulesActivity::class.java).apply {
            putExtra(EXTRA_DAY_CODE, dayCode)
            putExtra(EXTRA_NAME_DAY, nameDay)
        }, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val resultArrSchedule = data?.getStringExtra(EXTRA_ARR_SCHEDULE)
            when (resultCode) {
                0 -> saveArrSchedule(resultCode, resultArrSchedule)
                1 -> saveArrSchedule(resultCode, resultArrSchedule)
                2 -> saveArrSchedule(resultCode, resultArrSchedule)
                3 -> saveArrSchedule(resultCode, resultArrSchedule)
                4 -> saveArrSchedule(resultCode, resultArrSchedule)
                5 -> saveArrSchedule(resultCode, resultArrSchedule)
                6 -> saveArrSchedule(resultCode, resultArrSchedule)
            }
        }
    }

    private fun saveArrSchedule(dayCode: Int, arrSchedule: String?) {
        val sharedPrefSchedules =
            getSharedPreferences(GetSchedulesActivity::class.simpleName, Context.MODE_PRIVATE)
        sharedPrefSchedules.edit {
            putString("dayCode$dayCode", arrSchedule)
            Log.d("dayCode:$dayCode", arrSchedule!!)
            commit()
        }
    }
}