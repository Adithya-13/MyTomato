package com.extcode.project.mytomato.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.TaskViewPagerAdapter
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        configViewPager()

        backButton.setOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        configViewPager()
    }

    private fun configViewPager() {
        val taskViewPagerAdapter = TaskViewPagerAdapter(this, supportFragmentManager)
        taskViewPager.adapter = taskViewPagerAdapter
        tabs.setupWithViewPager(taskViewPager)
    }
}