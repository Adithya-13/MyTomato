package com.extcode.project.mytomato.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.extcode.project.mytomato.data.TaskData
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.DATE
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TABLE_NAME

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskData: TaskData): Long

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $DATE ASC")
    fun queryAll(): LiveData<List<TaskData>>

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id ")
    fun deleteTask(id: Int): Int

}