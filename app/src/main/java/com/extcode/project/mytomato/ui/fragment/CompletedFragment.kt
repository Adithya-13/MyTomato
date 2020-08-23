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
import com.extcode.project.mytomato.adapter.CompletedAdapter
import com.extcode.project.mytomato.data.CompletedData
import com.extcode.project.mytomato.ui.InputTaskActivity
import com.extcode.project.mytomato.viewModel.TaskViewModel
import kotlinx.android.synthetic.main.fragment_completed.*

class CompletedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed, container, false)
    }

    private lateinit var completedAdapter: CompletedAdapter
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecyclerView()
        queryAll()
    }

    private fun queryAll() {

        showProgressBar(true)
        showNotFound(false)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        taskViewModel.completedQueryAll(context as Context).observe(viewLifecycleOwner, Observer {

            if (!it.isNullOrEmpty()) {
                showNotFound(false)
                showProgressBar(false)
                completedAdapter.arrTask = it as ArrayList<CompletedData>
                completedAdapter.notifyDataSetChanged()
            } else {
                showProgressBar(false)
                showNotFound(true)
            }

        })
    }

    private fun configRecyclerView() {
        completedAdapter = CompletedAdapter()
        completedAdapter.notifyDataSetChanged()

        rvCompleted.layoutManager = LinearLayoutManager(context)
        rvCompleted.adapter = completedAdapter

        completedAdapter.setOnItemClickCallback(object : CompletedAdapter.OnItemClickCallback {
            override fun onItemClicked(completedData: CompletedData) {

                startActivity(Intent(context, InputTaskActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(InputTaskActivity.EXTRA_COMPLETED_DATA, completedData)
                    putExtra(InputTaskActivity.EXTRA_COMPLETED_CODE, true)
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