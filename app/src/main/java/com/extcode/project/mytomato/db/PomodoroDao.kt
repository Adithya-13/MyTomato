package com.extcode.project.mytomato.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.extcode.project.mytomato.data.PomodoroData
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TABLE_POMODORO_NAME

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPomodoro(pomodoroData: PomodoroData): Long

    @Query("SELECT * FROM $TABLE_POMODORO_NAME ORDER BY pomodoroCountGoals DESC")
    fun queryAll(): LiveData<List<PomodoroData>>

    @Query("DELETE FROM $TABLE_POMODORO_NAME WHERE pomodoroId = :id ")
    fun deletePomodoro(id: Int): Int

}