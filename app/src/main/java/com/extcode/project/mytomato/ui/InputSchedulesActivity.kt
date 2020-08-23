package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.SchedulesAdapter
import com.extcode.project.mytomato.data.ScheduleData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_input_schedules.*


class InputSchedulesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DAY_CODE = "dayCode"
        const val EXTRA_NAME_DAY = "nameDay"
        const val EXTRA_ARR_SCHEDULE = "arrSchedule"
    }

    private lateinit var schedulesAdapter: SchedulesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_schedules)

        val dayCode = intent.getIntExtra(EXTRA_DAY_CODE, 0)
        val nameDay = intent.getStringExtra(EXTRA_NAME_DAY)
        day.text = nameDay
        configRecyclerView(dayCode)
        checkArrScheduleSize()

        addSchedule.setOnClickListener {
            schedulesAdapter.arrSchedule.add(ScheduleData(null))
            schedulesAdapter.notifyDataSetChanged()
            checkArrScheduleSize()
        }

        saveSchedule.setOnClickListener {
            sendData(dayCode)
        }

        schedulesAdapter.setOnDeleteItem(object : SchedulesAdapter.OnDeleteItem {
            override fun deleteItem(isRemoved: Boolean) {
                if (isRemoved) checkArrScheduleSize()
            }
        })

    }

    private fun checkArrScheduleSize() {

        if (schedulesAdapter.arrSchedule.size > 0) {
            notFound.visibility = View.GONE
            tvAddSchedule.visibility = View.GONE
        } else {
            notFound.visibility = View.VISIBLE
            tvAddSchedule.visibility = View.VISIBLE
        }

    }

    private fun sendData(dayCode: Int) {

        var isScheduleDataEmpty = false
        for (i in 0 until schedulesAdapter.arrSchedule.size) {
            if (schedulesAdapter.arrSchedule[i].title.isNullOrEmpty()) {
                isScheduleDataEmpty = true
                break
            } else {
                isScheduleDataEmpty = false
            }
        }

        if (isScheduleDataEmpty)
            Toast.makeText(this, "Please Add the Schedules", Toast.LENGTH_SHORT).show()
        else {
            val gson = Gson()
            val json = gson.toJson(schedulesAdapter.arrSchedule)
            val intent = Intent()
            intent.apply {
                putExtra(EXTRA_ARR_SCHEDULE, json)
            }
            setResult(dayCode, intent)
            finish()

        }
    }

    private fun configRecyclerView(dayCode: Int) {

        schedulesAdapter = SchedulesAdapter()
        schedulesAdapter.arrSchedule = loadData(dayCode)
        schedulesAdapter.notifyDataSetChanged()

        scheduleRecyclerView.layoutManager = LinearLayoutManager(this)
        scheduleRecyclerView.adapter = schedulesAdapter

    }

    private fun loadData(dayCode: Int): ArrayList<ScheduleData> {

        val sharedPrefSchedules =
            getSharedPreferences(GetSchedulesActivity::class.simpleName, Context.MODE_PRIVATE)
        val json = sharedPrefSchedules.getString("dayCode$dayCode", null)
        val type = object : TypeToken<ArrayList<ScheduleData>>() {}.type

        return if (json.isNullOrEmpty()) ArrayList() else Gson().fromJson(json, type)
    }

    override fun onBackPressed() { saveSchedule.callOnClick() }
}