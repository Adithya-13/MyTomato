package com.extcode.project.mytomato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.PomodoroData
import kotlinx.android.synthetic.main.item_pomodoro.view.*
import kotlinx.android.synthetic.main.item_task.view.title

class PomodoroAdapter : RecyclerView.Adapter<PomodoroAdapter.TaskAdapterViewHolder>() {

    var arrPomodoro = ArrayList<PomodoroData>()
        set(value) {
            this.arrPomodoro.clear()
            this.arrPomodoro.addAll(value)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(pomodoroData: PomodoroData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapterViewHolder {
        return TaskAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_pomodoro, parent, false)
        )
    }

    override fun getItemCount(): Int = arrPomodoro.size

    override fun onBindViewHolder(holder: TaskAdapterViewHolder, position: Int) {
        holder.bind(arrPomodoro[position])
    }

    inner class TaskAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pomodoroData: PomodoroData) {
            with(itemView) {

                title.text = pomodoroData.title
                goalsTotal.text = pomodoroData.countGoals.toString()

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(pomodoroData) }
                itemView.animation = AnimationUtils.loadAnimation(context, R.anim.up_anim)

            }
        }
    }
}