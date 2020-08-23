package com.extcode.project.mytomato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.TaskData
import kotlinx.android.synthetic.main.item_task.view.*
import java.util.*
import kotlin.collections.ArrayList

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder>() {

    var arrTask = ArrayList<TaskData>()
        set(value) {
            this.arrTask.clear()
            this.arrTask.addAll(value)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(taskData: TaskData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapterViewHolder {
        return TaskAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        )
    }

    override fun getItemCount(): Int = arrTask.size

    override fun onBindViewHolder(holder: TaskAdapterViewHolder, position: Int) {
        holder.bind(arrTask[position])
    }

    inner class TaskAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taskData: TaskData) {
            with(itemView) {

                if (taskData.date == context.getString(R.string.isNull) && taskData.time == context.getString(R.string.isNull)) {
                    title.text = taskData.title
                    description.text = taskData.description
                    dlContainer.visibility = View.GONE
                } else {
                    dlContainer.visibility = View.VISIBLE
                    title.text = taskData.title
                    description.text = taskData.description
                    dlDate.text = taskData.date
                    dlTime.text = taskData.time

                    val arrDate = taskData.date!!.split("-").toTypedArray()
                    val arrTime = taskData.time!!.split(":").toTypedArray()

                    val calendar = Calendar.getInstance()
                    calendar.apply {
                        set(Calendar.YEAR, Integer.parseInt(arrDate[0]))
                        set(Calendar.MONTH, Integer.parseInt(arrDate[1]) - 1)
                        set(Calendar.DAY_OF_MONTH, Integer.parseInt(arrDate[2]))
                        set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTime[0]))
                        set(Calendar.MINUTE, Integer.parseInt(arrTime[1]))
                        set(Calendar.SECOND, 0)
                    }

                    val calendarTimeInMills = calendar.timeInMillis

                    val calendarNow = Calendar.getInstance()
                    calendarNow.apply {
                        get(Calendar.YEAR)
                        get(Calendar.MONTH)
                        get(Calendar.DAY_OF_MONTH)
                        get(Calendar.HOUR_OF_DAY)
                        get(Calendar.MINUTE)
                        get(Calendar.SECOND)
                    }

                    val calendarNowTimeInMills = calendarNow.timeInMillis

                    if (calendarTimeInMills <= calendarNowTimeInMills) {
                        dlDate.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.colorAccent,
                                null
                            )
                        )
                        dlTime.setTextColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.colorAccent,
                                null
                            )
                        )
                    }

                }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(taskData) }
                itemView.animation = AnimationUtils.loadAnimation(context, R.anim.up_anim)

            }
        }
    }
}