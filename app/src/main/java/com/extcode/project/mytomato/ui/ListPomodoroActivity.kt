package com.extcode.project.mytomato.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.PomodoroAdapter
import com.extcode.project.mytomato.data.PomodoroData
import com.extcode.project.mytomato.ui.PomodoroActivity.Companion.EXTRA_POMODORO_DATA
import com.extcode.project.mytomato.viewModel.PomodoroViewModel
import kotlinx.android.synthetic.main.activity_list_pomodoro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListPomodoroActivity : AppCompatActivity() {

    private lateinit var pomodoroAdapter: PomodoroAdapter
    private lateinit var pomodoroViewModel: PomodoroViewModel
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pomodoro)

        configRecyclerView()
        queryAll()

        backButton.setOnClickListener { onBackPressed() }
        deleteButton.setOnClickListener { toastIsEdit() }
        fabTask.setOnClickListener {
            startActivity(Intent(this, InputPomodoroActivity::class.java))
        }

    }

    private fun toastIsEdit() {

        isEdit = !isEdit
        if (isEdit) {
            Toast.makeText(
                this,
                "Select the Pomodoro timer you want to delete",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                "Delete mode is off",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun queryAll() {

        showProgressBar(true)
        showNotFound(false)

        pomodoroViewModel = ViewModelProvider(this).get(PomodoroViewModel::class.java)
        pomodoroViewModel.queryAll(this).observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                showNotFound(false)
                showProgressBar(false)
                pomodoroAdapter.arrPomodoro = it as ArrayList<PomodoroData>
                pomodoroAdapter.notifyDataSetChanged()
            } else {
                showProgressBar(false)
                showNotFound(true)
            }
        })
    }

    private fun configRecyclerView() {

        pomodoroAdapter = PomodoroAdapter()
        pomodoroAdapter.notifyDataSetChanged()

        rvPomodoro.layoutManager = LinearLayoutManager(this)
        rvPomodoro.adapter = pomodoroAdapter

        pomodoroAdapter.setOnItemClickCallback(object : PomodoroAdapter.OnItemClickCallback {
            override fun onItemClicked(pomodoroData: PomodoroData) {

                if (isEdit) {
                    edit(pomodoroData)
                } else {
                    startActivity(
                        Intent(
                            this@ListPomodoroActivity,
                            PomodoroActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            putExtra(EXTRA_POMODORO_DATA, pomodoroData)
                        }
                    )
                }

            }
        })
    }

    private fun edit(pomodoroData: PomodoroData) {

        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Dialog)

        alertDialogBuilder.setTitle("Delete Pomodoro")
        alertDialogBuilder.setMessage("Pomodoro Timer will be lost !!")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                try {
                    GlobalScope.launch(Dispatchers.IO) {
                        pomodoroViewModel =
                            ViewModelProvider(this@ListPomodoroActivity).get(PomodoroViewModel::class.java)
                        pomodoroViewModel.deletePomodoro(
                            this@ListPomodoroActivity,
                            pomodoroData.pomodoroId
                        )
                    }
                    isEdit = false
                    pomodoroAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(this, "Failed to delete Pomodoro Timer", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun showProgressBar(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showNotFound(isShow: Boolean) {
        notFound.visibility = if (isShow) View.VISIBLE else View.GONE
    }

}