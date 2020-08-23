package com.extcode.project.mytomato.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.TaskAdapter
import com.extcode.project.mytomato.data.TaskData
import com.extcode.project.mytomato.ui.InputTaskActivity
import com.extcode.project.mytomato.ui.InputTaskActivity.Companion.EXTRA_EDIT_CODE
import com.extcode.project.mytomato.ui.InputTaskActivity.Companion.EXTRA_TASK_DATA
import com.extcode.project.mytomato.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_on_going.*

class OnGoingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_on_going, container, false)
    }

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()
        queryAll()

        fabTask.setOnClickListener {
            startActivity(Intent(context, InputTaskActivity::class.java))
        }

    }

    private fun queryAll() {

        showProgressBar(true)
        showNotFound(false)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.queryAll(context as Context).observe(viewLifecycleOwner, Observer {

            if (!it.isNullOrEmpty()) {
                showNotFound(false)
                showProgressBar(false)
                taskAdapter.arrTask = it as ArrayList<TaskData>
                taskAdapter.notifyDataSetChanged()
            } else {
                showProgressBar(false)
                showNotFound(true)
            }

        })
    }

    private fun configRecyclerView() {

        taskAdapter = TaskAdapter()
        taskAdapter.notifyDataSetChanged()

        rvOnGoing.layoutManager = LinearLayoutManager(context)
        rvOnGoing.adapter = taskAdapter

        taskAdapter.setOnItemClickCallback(object : TaskAdapter.OnItemClickCallback {
            override fun onItemClicked(taskData: TaskData) {

                startActivity(Intent(context, InputTaskActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(EXTRA_TASK_DATA, taskData)
                    putExtra(EXTRA_EDIT_CODE, true)
                })

            }
        })
    }

    private fun showProgressBar(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showNotFound(isShow: Boolean) {
        notFound.visibility = if (isShow) View.VISIBLE else View.GONE
    }

}