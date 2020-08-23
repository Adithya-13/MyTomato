package com.extcode.project.mytomato.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.HomeScheduleAdapter
import com.extcode.project.mytomato.data.ScheduleData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_schedules.*

class SchedulesActivity : AppCompatActivity() {

    private lateinit var homeScheduleAdapter: HomeScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules)

        init()
        backButton.setOnClickListener { onBackPressed() }
    }

    private fun init() {
        configRecyclerView(0, rvMonday)
        configRecyclerView(1, rvTuesday)
        configRecyclerView(2, rvWednesday)
        configRecyclerView(3, rvThursday)
        configRecyclerView(4, rvFriday)
        configRecyclerView(5, rvSaturday)
        configRecyclerView(6, rvSunday)
    }

    private fun configRecyclerView(dayCode: Int, recyclerView: RecyclerView) {

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

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = homeScheduleAdapter

    }

    private fun loadData(dayCode: Int): ArrayList<ScheduleData> {
        val sharedPrefSchedules =
            getSharedPreferences(GetSchedulesActivity::class.simpleName, Context.MODE_PRIVATE)

        val json = sharedPrefSchedules.getString("dayCode$dayCode", null)
        val type = object : TypeToken<ArrayList<ScheduleData>>() {}.type

        return if (json.isNullOrEmpty()) ArrayList() else Gson().fromJson(json, type)
    }

}