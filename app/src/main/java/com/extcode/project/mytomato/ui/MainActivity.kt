package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.HomeScheduleAdapter
import com.extcode.project.mytomato.data.ScheduleData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var homeScheduleAdapter: HomeScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setName()
        getDay()

        news.setOnClickListener(this)
        schedules.setOnClickListener(this)
        pomodoro.setOnClickListener(this)
        task.setOnClickListener(this)
        settings.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        setName()
        getDay()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.news -> intentActivity(NewsActivity::class.java)
            R.id.schedules -> intentActivity(SchedulesActivity::class.java)
            R.id.pomodoro -> intentActivity(ListPomodoroActivity::class.java)
            R.id.task -> intentActivity(TaskActivity::class.java)
            R.id.settings -> {
                intentActivity(SettingActivity::class.java)
                overridePendingTransition(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
            }
        }
    }

    private fun getDay() {
        val calendar = Calendar.getInstance()
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> configRecyclerView(0, "Monday")
            Calendar.TUESDAY -> configRecyclerView(1, "Tuesday")
            Calendar.WEDNESDAY -> configRecyclerView(2, "Wednesday")
            Calendar.THURSDAY -> configRecyclerView(3, "Thursday")
            Calendar.FRIDAY -> configRecyclerView(4, "Friday")
            Calendar.SATURDAY -> configRecyclerView(5, "Saturday")
            Calendar.SUNDAY -> configRecyclerView(6, "Sunday")
        }
    }

    private fun configRecyclerView(dayCode: Int, day: String) {

        homeScheduleAdapter = HomeScheduleAdapter()

        if (loadData(dayCode).isNullOrEmpty()) {

            val arrHomeSchedule = ArrayList<ScheduleData>()
            arrHomeSchedule.add(ScheduleData("You have no Schedule today!!"))

            homeScheduleAdapter.arrHomeSchedule = arrHomeSchedule
            homeScheduleAdapter.notifyDataSetChanged()

        } else {

            homeScheduleAdapter.arrHomeSchedule = loadData(dayCode)
            homeScheduleAdapter.notifyDataSetChanged()

        }

        recyclerViewHomeSchedule.layoutManager = LinearLayoutManager(this)
        recyclerViewHomeSchedule.adapter = homeScheduleAdapter

        dayName.text = day.toUpperCase(Locale.getDefault())
    }

    private fun loadData(dayCode: Int): ArrayList<ScheduleData> {
        val sharedPrefSchedules =
            getSharedPreferences(GetSchedulesActivity::class.simpleName, Context.MODE_PRIVATE)

        val json = sharedPrefSchedules.getString("dayCode$dayCode", null)
        val type = object : TypeToken<ArrayList<ScheduleData>>() {}.type

        return if (json.isNullOrEmpty()) ArrayList() else Gson().fromJson(json, type)
    }

    private fun setName() {

        getSharedPreferences(
            NameActivity::class.simpleName,
            Context.MODE_PRIVATE
        ).apply {
            val fullName = getString("name", null)
            val firstName = fullName?.split(" ")?.toTypedArray()
            nameTopBar.text = getString(R.string.hi_s_today_is, firstName?.get(0))
        }

    }

    private fun intentActivity(cls: Class<*>) {
        startActivity(Intent(this, cls).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        })
    }
}