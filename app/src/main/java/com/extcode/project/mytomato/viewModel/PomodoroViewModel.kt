package com.extcode.project.mytomato.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.extcode.project.mytomato.data.PomodoroData
import com.extcode.project.mytomato.db.TaskDatabase

class PomodoroViewModel : ViewModel() {

    fun queryAll(context: Context): LiveData<List<PomodoroData>> {
        val db = TaskDatabase.getInstance(context)
        return db.pomodoroDao().queryAll()
    }

    fun deletePomodoro(context: Context, id: Int): Int {
        val db = TaskDatabase.getInstance(context)
        return db.pomodoroDao().deletePomodoro(id)
    }

    fun insertPomodoro(context: Context, pomodoroData: PomodoroData): Long {
        val db = TaskDatabase.getInstance(context)
        return db.pomodoroDao().insertPomodoro(pomodoroData)
    }

}