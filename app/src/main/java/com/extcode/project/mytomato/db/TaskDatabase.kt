package com.extcode.project.mytomato.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.extcode.project.mytomato.data.CompletedData
import com.extcode.project.mytomato.data.PomodoroData
import com.extcode.project.mytomato.data.TaskData
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.DATABASE_NAME

@Database(
    entities = [TaskData::class, CompletedData::class, PomodoroData::class],
    version = 4,
    exportSchema = true
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun completedDao(): CompletedDao
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}