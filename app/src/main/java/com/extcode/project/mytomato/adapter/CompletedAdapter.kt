package com.extcode.project.mytomato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.CompletedData
import kotlinx.android.synthetic.main.item_task.view.*

class CompletedAdapter : RecyclerView.Adapter<CompletedAdapter.TaskAdapterViewHolder>() {

    var arrTask = ArrayList<CompletedData>()
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
        fun onItemClicked(completedData: CompletedData)
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
        fun bind(completedData: CompletedData) {
            with(itemView) {

                if (completedData.date == context.getString(R.string.isNull) && completedData.time == context.getString(R.string.isNull)) {
                    title.text = completedData.title
                    description.text = completedData.description
                    dlContainer.visibility = View.GONE
                } else {
                    dlContainer.visibility = View.VISIBLE
                    title.text = completedData.title
                    description.text = completedData.description
                    dlDate.text = completedData.date
                    dlTime.text = completedData.time
                }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(completedData) }
                itemView.animation = AnimationUtils.loadAnimation(context, R.anim.up_anim)

            }
        }
    }
}