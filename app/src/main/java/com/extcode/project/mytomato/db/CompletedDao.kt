package com.extcode.project.mytomato.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.extcode.project.mytomato.data.CompletedData
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_DATE
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_ID
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TABLE_COMPLETED_NAME

@Dao
interface CompletedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(completedData: CompletedData): Long

    @Query("SELECT * FROM $TABLE_COMPLETED_NAME ORDER BY $COMPLETED_DATE ASC")
    fun queryAll(): LiveData<List<CompletedData>>

    @Query("DELETE FROM $TABLE_COMPLETED_NAME WHERE $COMPLETED_ID = :id ")
    fun deleteTask(id: Int): Int

    @Update
    fun updateTask(completedData: CompletedData): Int
}