package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.PomodoroData
import com.extcode.project.mytomato.viewModel.PomodoroViewModel
import kotlinx.android.synthetic.main.activity_input_pomodoro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InputPomodoroActivity : AppCompatActivity() {

    private lateinit var pomodoroViewModel: PomodoroViewModel
    private lateinit var pomodoroData: PomodoroData
    private lateinit var pomodoroSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_pomodoro)

        pomodoroSharedPreferences =
            getSharedPreferences(InputPomodoroActivity::class.simpleName, Context.MODE_PRIVATE)

        savePomodoro.setOnClickListener { savePomodoro() }

    }

    private fun savePomodoro() {

        val title = titlePomodoro.editText?.text.toString()
        var id = pomodoroSharedPreferences.getInt("id", 0)

        pomodoroData = PomodoroData(id, title)

        if (title.isEmpty()) toastError("Title must not be null!")
        else {
            try {

                pomodoroSharedPreferences.edit { putInt("id", ++id) }
                GlobalScope.launch(Dispatchers.IO) {
                    pomodoroViewModel =
                        ViewModelProvider(this@InputPomodoroActivity).get(
                            PomodoroViewModel::class.java
                        )
                    pomodoroViewModel.insertPomodoro(this@InputPomodoroActivity, pomodoroData)
                }
                finish()

            } catch (e: Exception) {
                toastError("Failed to Save Task")
            }
        }
    }

    private fun toastError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}