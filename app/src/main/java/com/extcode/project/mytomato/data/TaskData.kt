package com.extcode.project.mytomato.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.DATE
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.DESCRIPTION
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TABLE_NAME
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TIME
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TITLE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class TaskData(

    @ColumnInfo
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = TITLE) var title: String?,
    @ColumnInfo(name = DESCRIPTION) var description: String?,
    @ColumnInfo(name = DATE) var date: String?,
    @ColumnInfo(name = TIME) var time: String?

) : Parcelable