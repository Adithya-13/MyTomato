package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.CompletedData
import com.extcode.project.mytomato.data.TaskData
import com.extcode.project.mytomato.service.AlarmReceiver
import com.extcode.project.mytomato.ui.fragment.DatePickerFragment
import com.extcode.project.mytomato.ui.fragment.TimePickerFragment
import com.extcode.project.mytomato.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.activity_input_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InputTaskActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    companion object {
        const val EXTRA_TASK_DATA = "extraTaskData"
        const val EXTRA_EDIT_CODE = "extraEditCode"
        const val EXTRA_COMPLETED_CODE = "extraCompletedCode"
        const val EXTRA_COMPLETED_DATA = "extraCompletedData"
    }

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskData: TaskData
    private lateinit var completedData: CompletedData
    private lateinit var taskSharedPref: SharedPreferences
    private lateinit var getTaskData: TaskData
    private lateinit var getCompletedData: CompletedData
    private lateinit var alarmReceiver: AlarmReceiver
    private var isDoubleBack = false
    private var isEdit = false
    private var isFromCompleteTask = false
    private var switchOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_task)

        taskSharedPref =
            getSharedPreferences(InputTaskActivity::class.simpleName, Context.MODE_PRIVATE)
        isEdit = intent.getBooleanExtra(EXTRA_EDIT_CODE, false)
        isFromCompleteTask = intent.getBooleanExtra(EXTRA_COMPLETED_CODE, false)
        alarmReceiver = AlarmReceiver()

        checkSwitch()
        configIsEdit()

        saveTask.setOnClickListener(this)
        deadLineDate.setOnClickListener(this)
        deadLineTime.setOnClickListener(this)
        deleteTask.setOnClickListener(this)
        completeTask.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        val datePickerFragment = DatePickerFragment()
        val timePickerFragment = TimePickerFragment()

        when (v?.id) {
            R.id.deadLineDate -> datePickerFragment.show(supportFragmentManager, "datePicker")
            R.id.deadLineTime -> timePickerFragment.show(supportFragmentManager, "timePicker")
            R.id.deleteTask -> showAlertDialog()
            R.id.completeTask -> completeTask()
            R.id.saveTask -> saveTask()
        }
    }

    private fun checkSwitch() {
        dlSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                deadLineDate.visibility = View.VISIBLE
                deadLineTime.visibility = View.VISIBLE
                switchOn = true
            } else {
                deadLineDate.visibility = View.GONE
                deadLineTime.visibility = View.GONE
                deadLineDate.text = getString(R.string.date)
                deadLineTime.text = getString(R.string.time)
                switchOn = false
            }
        }
    }

    private fun configIsEdit() {
        if (isEdit) {

            deleteTask.visibility = View.VISIBLE
            completeTask.visibility = View.VISIBLE

            getTaskData = intent?.getParcelableExtra(EXTRA_TASK_DATA)!!
            titleTask.editText?.setText(getTaskData.title)
            descriptionTask.editText?.setText(getTaskData.description)

            if (getTaskData.date == "null" && getTaskData.time == "null") {
                dlSwitch.isChecked = false
            } else {
                dlSwitch.isChecked = true

                deadLineDate.text = getTaskData.date
                deadLineTime.text = getTaskData.time
            }
        } else if (isFromCompleteTask) {

            deleteTask.visibility = View.VISIBLE
            completeTask.visibility = View.GONE

            getCompletedData = intent?.getParcelableExtra(EXTRA_COMPLETED_DATA)!!
            titleTask.editText?.setText(getCompletedData.title)
            descriptionTask.editText?.setText(getCompletedData.description)

            if (getCompletedData.date == "null" && getCompletedData.time == "null") {
                dlSwitch.isChecked = false
            } else {
                dlSwitch.isChecked = true

                deadLineDate.text = getCompletedData.date
                deadLineTime.text = getCompletedData.time
            }

        } else {
            deleteTask.visibility = View.GONE
            completeTask.visibility = View.GONE
        }
    }

    private fun saveTask() {

        val title = titleTask.editText?.text.toString()
        val description = descriptionTask.editText?.text.toString()
        val date = deadLineDate.text.toString()
        val time = deadLineTime.text.toString()

        var id = taskSharedPref.getInt("id", 3)

        if (switchOn) {
            taskData =
                if (isEdit) TaskData(
                    getTaskData.id,
                    title,
                    description,
                    date,
                    time
                ) else TaskData(
                    id,
                    title,
                    description,
                    date,
                    time
                )

            when {

                title.isEmpty() -> toastError("Title must not be null!")
                description.isEmpty() -> toastError("Description must not be null!")
                date == "Date" -> toastError("Date must not be null!")
                time == "Time" -> toastError("Time must not be null!")

                else -> {

                    try {
                        if (isFromCompleteTask) {
                            getCompletedData = intent?.getParcelableExtra(EXTRA_COMPLETED_DATA)!!

                            completedData = CompletedData(
                                getCompletedData.id,
                                title,
                                description,
                                date,
                                time
                            )
                            insertCompletedData()
                        } else {

                            taskSharedPref.edit { putInt("id", ++id) }
                            if (isEdit) {

                                alarmReceiver.cancelAlarm(this, taskData.id)
                                alarmReceiver.setAlarm(
                                    this,
                                    "Your Task will end in 1 hour",
                                    "${taskData.title} will end in ${taskData.time}, ${taskData.date}",
                                    taskData.id,
                                    taskData.date!!,
                                    taskData.time!!
                                )

                            } else {
                                alarmReceiver.setAlarm(
                                    this,
                                    "Your Task will end in 1 hour",
                                    "${taskData.title} will end in ${taskData.time}, ${taskData.date}",
                                    taskData.id,
                                    taskData.date!!,
                                    taskData.time!!
                                )
                            }
                            insertTask()
                        }
                        finish()
                    } catch (e: Exception) {
                        toastError("Failed to Save Task")
                        Log.d("saveTask", e.message!!)
                    }
                }
            }
        } else {

            taskData =
                if (isEdit) TaskData(
                    getTaskData.id,
                    title,
                    description,
                    "null",
                    "null"
                ) else TaskData(
                    id,
                    title,
                    description,
                    "null",
                    "null"
                )

            when {

                title.isEmpty() -> toastError("Title must not be null!")
                description.isEmpty() -> toastError("Description must not be null!")

                else -> {
                    try {
                        if (isFromCompleteTask) {

                            getCompletedData = intent?.getParcelableExtra(EXTRA_COMPLETED_DATA)!!

                            completedData = CompletedData(
                                getCompletedData.id,
                                title,
                                description,
                                "null",
                                "null"
                            )
                            insertCompletedData()
                        } else {
                            taskSharedPref.edit { putInt("id", ++id) }
                            insertTask()
                        }
                        finish()
                    } catch (e: Exception) {
                        toastError("Failed to Save Task")
                    }
                }
            }
        }
    }

    private fun insertTask() {
        GlobalScope.launch(Dispatchers.IO) {
            taskViewModel =
                ViewModelProvider(this@InputTaskActivity).get(
                    TaskViewModel::class.java
                )
            taskViewModel.insertTask(this@InputTaskActivity, taskData)
        }
    }

    private fun insertCompletedData() {
        GlobalScope.launch(Dispatchers.IO) {
            taskViewModel =
                ViewModelProvider(this@InputTaskActivity).get(
                    TaskViewModel::class.java
                )

            taskViewModel.insertCompleted(
                this@InputTaskActivity,
                completedData
            )
        }
    }

    private fun completeTask() {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                taskViewModel =
                    ViewModelProvider(this@InputTaskActivity).get(TaskViewModel::class.java)
                val id = getTaskData.id
                val title = getTaskData.title
                val description = getTaskData.description
                val date = getTaskData.date
                val time = getTaskData.time

                taskViewModel.deleteTask(this@InputTaskActivity, getTaskData.id)
                completedData = CompletedData(id, title, description, date, time)
                taskViewModel.insertCompleted(this@InputTaskActivity, completedData)
            }
            finish()
        } catch (e: Exception) {
            toastError("Failed to Completed Task")
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        deadLineDate.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        deadLineTime.text = dateFormat.format(calendar.time)
    }

    override fun onBackPressed() {
        if (isDoubleBack) {
            super.onBackPressed()
            return
        }

        this.isDoubleBack = true
        Toast.makeText(
            this,
            "The task you entered will not saved, back again to exit !!",
            Toast.LENGTH_LONG
        ).show()

        Handler().postDelayed({ isDoubleBack = false }, 2000)

    }

    private fun showAlertDialog() {

        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Delete Task")
        alertDialogBuilder.setMessage("The task you entered will be lost !!")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                try {
                    if (isFromCompleteTask) {
                        getCompletedData =
                            intent?.getParcelableExtra(EXTRA_COMPLETED_DATA)!!
                        GlobalScope.launch(Dispatchers.IO) {
                            taskViewModel =
                                ViewModelProvider(this@InputTaskActivity).get(TaskViewModel::class.java)
                            taskViewModel.deleteCompleted(
                                this@InputTaskActivity,
                                getCompletedData.id
                            )
                        }
                    } else {
                        GlobalScope.launch(Dispatchers.IO) {
                            taskViewModel =
                                ViewModelProvider(this@InputTaskActivity).get(TaskViewModel::class.java)
                            taskViewModel.deleteTask(this@InputTaskActivity, getTaskData.id)
                        }
                    }
                    finish()
                } catch (e: Exception) {
                    toastError("Failed to Delete Task")
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun toastError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}