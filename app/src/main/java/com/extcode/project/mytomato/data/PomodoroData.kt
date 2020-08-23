package com.extcode.project.mytomato.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_COUNT_GOALS
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_COUNT_ROUNDS
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_IS_BREAK
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_IS_LONG_BREAK
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_IS_RUNNING
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_M_END_TIME
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_M_TIME_LEFT_IN_MILLIS
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_START_TIME_IN_MILLIS
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.POMODORO_TITLE
import com.extcode.project.mytomato.db.DatabaseContract.TaskColumn.Companion.TABLE_POMODORO_NAME
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_POMODORO_NAME)
data class PomodoroData(

    @PrimaryKey
    @ColumnInfo var pomodoroId: Int = 0,
    @ColumnInfo(name = POMODORO_TITLE) var title: String?,
    @ColumnInfo(name = POMODORO_M_TIME_LEFT_IN_MILLIS) var mTimeLeftInMillis: Long = 0,
    @ColumnInfo(name = POMODORO_START_TIME_IN_MILLIS) var startTimeInMillis: Long = 1500000,
    @ColumnInfo(name = POMODORO_M_END_TIME) var mEndTime: Long = 0,
    @ColumnInfo(name = POMODORO_IS_RUNNING) var isRunning: Boolean = false,
    @ColumnInfo(name = POMODORO_IS_BREAK) var isBreak: Boolean = false,
    @ColumnInfo(name = POMODORO_IS_LONG_BREAK) var isLongBreak: Boolean = false,
    @ColumnInfo(name = POMODORO_COUNT_ROUNDS) var countRounds: Int = 0,
    @ColumnInfo(name = POMODORO_COUNT_GOALS) var countGoals: Int = 0

) : Parcelable