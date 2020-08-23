package com.extcode.project.mytomato.ui

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.PomodoroData
import com.extcode.project.mytomato.service.AlarmReceiver
import com.extcode.project.mytomato.viewModel.PomodoroViewModel
import kotlinx.android.synthetic.main.activity_pomodoro.*
import kotlinx.android.synthetic.main.layout_pomodoro_timer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class PomodoroActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_POMODORO_DATA = "extraPomodoroData"
    }

    private lateinit var pomodoroViewModel: PomodoroViewModel
    private lateinit var pomodoroData: PomodoroData

    private lateinit var mCountDownTimer: CountDownTimer
    private lateinit var pomodoroSharedPref: SharedPreferences
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var alarmReceiver: AlarmReceiver
    private var mTimeLeftInMills: Long = 0
    private var startTimeInMillis: Long = 1500000
    private var mEndTime: Long = 0
    private var isRunning = false
    private var isBreak = false
    private var isLongBreak = false
    private val maxCountRounds = 4
    private var countRounds = 0
    private var countGoals = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pomodoro)

        pomodoroSharedPref =
            getSharedPreferences(PomodoroActivity::class.simpleName, Context.MODE_PRIVATE)
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        alarmReceiver = AlarmReceiver()

        pomodoroData = intent?.getParcelableExtra(EXTRA_POMODORO_DATA)!!

        backButton.setOnClickListener(this)
        startTimer.setOnClickListener(this)
        resetTimer.setOnClickListener(this)
        doneBreak.setOnClickListener(this)

        updateCountDownText()
        updateRounds()
        updateGoals()
    }

    override fun onPause() {
        super.onPause()

        pomodoroData = PomodoroData(
            pomodoroData.pomodoroId,
            pomodoroData.title,
            mTimeLeftInMills,
            startTimeInMillis,
            mEndTime,
            isRunning,
            isBreak,
            isLongBreak,
            countRounds,
            countGoals
        )

        insert()

        if (isRunning) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendar.timeInMillis + mTimeLeftInMills
            val showTimeLeft = SimpleDateFormat("HH:mm", Locale.getDefault())

            alarmReceiver.setAlarmPomodoro(
                this,
                if (isBreak) "Break timer is Running" else "Pomodoro timer is running",
                if (isBreak) "Break Will end in ${showTimeLeft.format(calendar.time)}" else "Pomodoro Will end in ${showTimeLeft.format(
                    calendar.time
                )}",
                0
            )
            mCountDownTimer.cancel()
        }
    }

    override fun onResume() {
        super.onResume()

        activityName.text = pomodoroData.title

        mTimeLeftInMills = pomodoroData.mTimeLeftInMillis
        isRunning = pomodoroData.isRunning
        isBreak = pomodoroData.isBreak
        isLongBreak = pomodoroData.isLongBreak
        startTimeInMillis = pomodoroData.startTimeInMillis

        updateCountDownText()
        updateButton()

        if (isRunning) {

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(0)

            mEndTime = pomodoroData.mEndTime
            mTimeLeftInMills = mEndTime - System.currentTimeMillis()

            if (mTimeLeftInMills <= 0) {
                mTimeLeftInMills = 0

                if (!isBreak) {
                    dndOff()
                    checkRound()
                }

                pomodoroData = PomodoroData(
                    pomodoroData.pomodoroId,
                    pomodoroData.title,
                    pomodoroData.mTimeLeftInMillis,
                    pomodoroData.startTimeInMillis,
                    pomodoroData.mEndTime,
                    pomodoroData.isRunning,
                    pomodoroData.isBreak,
                    pomodoroData.isLongBreak,
                    countRounds,
                    pomodoroData.countGoals
                )

                insert()
                updateRounds()
                isRunning = false
                isBreak = startTimeInMillis == 1500000.toLong()
                isLongBreak = countRounds == maxCountRounds
                updateCountDownText()
                updateButton()

            } else {
                start()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backButton -> finish()
            R.id.startTimer -> if (isRunning) pause() else start()
            R.id.resetTimer -> reset()
            R.id.doneBreak -> {
                if (isRunning) pause()
                isBreak = false
                reset()
            }
        }
    }

    private fun start() {

        mEndTime = System.currentTimeMillis() + mTimeLeftInMills

        if (!isBreak) if (!isRunning) showAlertDialog()

        mCountDownTimer = object : CountDownTimer(mTimeLeftInMills, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMills = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {

                if (!isBreak) {
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        checkRound()
                    }
                }

                pomodoroData = PomodoroData(
                    pomodoroData.pomodoroId,
                    pomodoroData.title,
                    pomodoroData.mTimeLeftInMillis,
                    pomodoroData.startTimeInMillis,
                    pomodoroData.mEndTime,
                    pomodoroData.isRunning,
                    pomodoroData.isBreak,
                    pomodoroData.isLongBreak,
                    countRounds,
                    pomodoroData.countGoals
                )

                insert()
                updateRounds()
                isRunning = false
                updateButton()
                isBreak = startTimeInMillis == 15000.toLong()
                isLongBreak = countRounds == maxCountRounds

            }
        }.start()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendar.timeInMillis + mTimeLeftInMills
        val showTimeLeft = SimpleDateFormat("HH:mm", Locale.getDefault())

        alarmReceiver.cancelAlarm(this, 1)
        alarmReceiver.setAlarmPomodoroEnd(
            this,
            if (isBreak) "Break timer is Done" else "Pomodoro timer is Done",
            if (isBreak) "Break end in ${showTimeLeft.format(calendar.time)}" else "Pomodoro end in ${showTimeLeft.format(
                calendar.time
            )}",
            1, calendar
        )

        isRunning = true
        updateButton()
    }

    private fun pause() {
        dndOff()
        mCountDownTimer.cancel()
        alarmReceiver = AlarmReceiver()
        alarmReceiver.cancelAlarm(this, 1)
        isRunning = false
        updateButton()
    }

    private fun reset() {
        startTimeInMillis = if (isBreak) if (isLongBreak) 1200000 else 300000 else 1500000
        mTimeLeftInMills = startTimeInMillis
        updateCountDownText()
        updateButton()
    }

    private fun insert() {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                pomodoroViewModel =
                    ViewModelProvider(this@PomodoroActivity).get(
                        PomodoroViewModel::class.java
                    )
                pomodoroViewModel.insertPomodoro(this@PomodoroActivity, pomodoroData)
            }
        } catch (e: Exception) {
            toastError()
        }
    }

    private fun checkRound() {
        if (countRounds < maxCountRounds) countRounds++ else {
            countRounds = 1
            countGoals++
            pomodoroData = PomodoroData(
                pomodoroData.pomodoroId,
                pomodoroData.title,
                pomodoroData.mTimeLeftInMillis,
                pomodoroData.startTimeInMillis,
                pomodoroData.mEndTime,
                pomodoroData.isRunning,
                pomodoroData.isBreak,
                pomodoroData.isLongBreak,
                pomodoroData.countRounds,
                countGoals
            )
            insert()
            updateGoals()
        }
        dndOff()
    }

    private fun dndOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_ALL)
            Toast.makeText(
                this@PomodoroActivity,
                "DND Mode off",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(
                this@PomodoroActivity,
                "Sorry, your Phone is doesn't support DND",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun dndOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            changeInterruptionFiler(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
            if (mNotificationManager.isNotificationPolicyAccessGranted) {
                Toast.makeText(this, "DND Mode on, Happy studying :D", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(
                this,
                "Sorry, your Phone is doesn't support DND",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMills / 1000) / 60
        val seconds = (mTimeLeftInMills / 1000) % 60
        val timeLeftFormatted =
            String.format(Locale.getDefault(), "%02d:%02d", minutes.toInt(), seconds.toInt())
        timer.text = timeLeftFormatted
    }

    private fun updateButton() {

        val enter = AnimationUtils.loadAnimation(this, R.anim.enter_button)
        val close = AnimationUtils.loadAnimation(this, R.anim.close_button)
        if (isRunning) {
            resetTimer.visibility = View.GONE
            resetTimer.animation = close
            startTimer.animation = enter
            startTimer.text = getString(R.string.pause)
        } else {
            startTimer.animation = close
            startTimer.text = getString(R.string.start)
            if (mTimeLeftInMills < 1000) {
                startTimer.visibility = View.GONE
                startTimer.animation = close
            } else {
                startTimer.visibility = View.VISIBLE
                startTimer.animation = enter
            }

            if (mTimeLeftInMills < startTimeInMillis) {
                resetTimer.visibility = View.VISIBLE
                resetTimer.animation = enter
            } else {
                resetTimer.visibility = View.GONE
                resetTimer.animation = close
            }
        }
        tvExplain.text =
            if (isBreak) if (isLongBreak) "Time for a long break !!" else "Time for a break !!" else "Time to study !!"
        doneBreak.visibility =
            if (isBreak) if (isLongBreak) View.VISIBLE else View.VISIBLE else View.GONE

    }

    private fun updateRounds() {
        countRounds = pomodoroData.countRounds
        rounds.text = getString(R.string.d_4, countRounds)
    }

    private fun updateGoals() {
        countGoals = pomodoroData.countGoals
        goals.text = getString(R.string.d, countGoals)
    }

    private fun showAlertDialog() {

        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Do Not Disturb Mode")
        alertDialogBuilder.setMessage("Do you agree to activate do not disturb mode while studying?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                dndOn()
                dialog.cancel()
            }
            .setNegativeButton("No") { dialog, _ ->
                dndOff()
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun changeInterruptionFiler(interruptionFilter: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mNotificationManager.isNotificationPolicyAccessGranted) {
                mNotificationManager.setInterruptionFilter(interruptionFilter)
            } else {
                Toast.makeText(this, "Search MyTomato App and turn on please :D", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "Sorry, your Phone is doesn't support DND", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun toastError() {
        Toast.makeText(this, "Failed to Save Pomodoro", Toast.LENGTH_SHORT).show()
    }

}