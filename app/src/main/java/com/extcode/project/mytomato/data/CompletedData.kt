package com.extcode.project.mytomato.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_DATE
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_DESCRIPTION
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_ID
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_TIME
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.COMPLETED_TITLE
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TABLE_COMPLETED_NAME
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_COMPLETED_NAME)
data class CompletedData(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COMPLETED_ID) val id: Int = 0,
    @ColumnInfo(name = COMPLETED_TITLE) var title: String?,
    @ColumnInfo(name = COMPLETED_DESCRIPTION) var description: String?,
    @ColumnInfo(name = COMPLETED_DATE) var date: String?,
    @ColumnInfo(name = COMPLETED_TIME) var time: String?

) : Parcelable