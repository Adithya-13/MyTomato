package com.extcode.project.mytomato.db

import android.provider.BaseColumns

object DatabaseContract {

    class TaskColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "taskTable"
            const val DATABASE_NAME = "taskDatabase"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val DATE = "date"
            const val TIME = "time"

            const val TABLE_COMPLETED_NAME = "completedTable"
            const val COMPLETED_ID = "completeId"
            const val COMPLETED_TITLE = "completedTitle"
            const val COMPLETED_DESCRIPTION = "completedDescription"
            const val COMPLETED_DATE = "completedDate"
            const val COMPLETED_TIME = "completedTime"

            const val TABLE_POMODORO_NAME = "pomodoroTable"
            const val POMODORO_TITLE = "pomodoroTitle"
            const val POMODORO_M_TIME_LEFT_IN_MILLIS = "pomodoroMTimeLeftInMillis"
            const val POMODORO_START_TIME_IN_MILLIS = "pomodoroStartTimeInMillis"
            const val POMODORO_M_END_TIME = "pomodoroMEndTime"
            const val POMODORO_IS_RUNNING = "pomodoroIsRunning"
            const val POMODORO_IS_BREAK = "pomodoroIsBreak"
            const val POMODORO_IS_LONG_BREAK = "pomodoroIsLongBreak"
            const val POMODORO_COUNT_ROUNDS = "pomodoroCountRounds"
            const val POMODORO_COUNT_GOALS = "pomodoroCountGoals"
        }
    }

}