package com.extcode.project.mytomato.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.ui.SplashScreenActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TITLE = "extraTitle"
        const val EXTRA_MESSAGE = "extraMessage"
        const val EXTRA_ID = "extraID"
        const val EXTRA_ON_GOING = "extraOnGoing"
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val notifId = intent.getIntExtra(EXTRA_ID, 0)
        val onGoing = intent.getBooleanExtra(EXTRA_ON_GOING, false)

        showAlarmNotification(context, title, message, onGoing, notifId)
    }

    fun setAlarm(
        context: Context,
        title: String?,
        message: String?,
        id: Int,
        date: String,
        time: String
    ) {

        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_ID, id)
            putExtra(EXTRA_ON_GOING, false)
        }

        val arrDate = date.split("-").toTypedArray()
        val arrTime = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.YEAR, Integer.parseInt(arrDate[0]))
            set(Calendar.MONTH, Integer.parseInt(arrDate[1]) - 1)
            set(Calendar.DAY_OF_MONTH, Integer.parseInt(arrDate[2]))
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTime[0]))
            set(Calendar.MINUTE, Integer.parseInt(arrTime[1]))
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis - 3600000, pendingIntent)

        Toast.makeText(context, "Alarm set up", Toast.LENGTH_SHORT).show()

    }

    fun setAlarmPomodoro(
        context: Context,
        title: String?,
        message: String?,
        id: Int
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_ID, id)
            putExtra(EXTRA_ON_GOING, true)
        }

        val calendar = Calendar.getInstance()

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

    fun setAlarmPomodoroEnd(
        context: Context,
        title: String?,
        message: String?,
        id: Int,
        calendar: Calendar
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_ID, id)
            putExtra(EXTRA_ON_GOING, false)
        }

        cancelAlarm(context, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

    private fun showAlarmNotification(
        context: Context,
        title: String?,
        message: String?,
        onGoing: Boolean,
        notifId: Int
    ) {

        val channelId = "TASK_1"
        val channelName = "TaskAlarmManager"

        val notificationIntent = Intent(context, SplashScreenActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            notifId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_round_access_time_24)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setOngoing(onGoing)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            }

            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)

        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)

    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }


    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}
