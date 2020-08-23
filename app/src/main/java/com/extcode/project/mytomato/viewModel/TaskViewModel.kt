package com.extcode.project.mytomato.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.extcode.project.mytomato.data.CompletedData
import com.extcode.project.mytomato.data.TaskData
import com.extcode.project.mytomato.db.TaskDatabase

class TaskViewModel : ViewModel() {

    fun queryAll(context: Context): LiveData<List<TaskData>> {
        val db = TaskDatabase.getInstance(context)
        return db.taskDao().queryAll()
    }

    fun deleteTask(context: Context, id: Int): Int {
        val db = TaskDatabase.getInstance(context)
        return db.taskDao().deleteTask(id)
    }

    fun insertTask(context: Context, taskData: TaskData): Long {
        val db = TaskDatabase.getInstance(context)
        return db.taskDao().insertTask(taskData)
    }

    fun completedQueryAll(context: Context): LiveData<List<CompletedData>> {
        val db = TaskDatabase.getInstance(context)
        return db.completedDao().queryAll()
    }

    fun deleteCompleted(context: Context, id: Int): Int {
        val db = TaskDatabase.getInstance(context)
        return db.completedDao().deleteTask(id)
    }

    fun insertCompleted(context: Context, completedData: CompletedData): Long {
        val db = TaskDatabase.getInstance(context)
        return db.completedDao().insertTask(completedData)
    }

}