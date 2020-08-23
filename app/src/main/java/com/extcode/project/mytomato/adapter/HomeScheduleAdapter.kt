package com.extcode.project.mytomato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.ScheduleData
import kotlinx.android.synthetic.main.item_get_schedules.view.*

class HomeScheduleAdapter : RecyclerView.Adapter<HomeScheduleAdapter.HomeScheduleViewHolder>() {

    var arrHomeSchedule = ArrayList<ScheduleData>()
        set(value) {
            this.arrHomeSchedule.clear()
            this.arrHomeSchedule.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScheduleViewHolder {
        return HomeScheduleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_get_schedules, parent, false)
        )
    }

    override fun getItemCount(): Int = arrHomeSchedule.size

    override fun onBindViewHolder(holder: HomeScheduleViewHolder, position: Int) {
        holder.bind(arrHomeSchedule[position])
    }

    inner class HomeScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(scheduleData: ScheduleData) {
            with(itemView) {

                if (arrHomeSchedule.size == 1 && arrHomeSchedule[0].title == context.getString(R.string.no_schedules)) {
                    tvScheduleItem.textSize = 20F
                    tvScheduleItem.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    tvScheduleItem.text = scheduleData.title
                } else {
                    tvScheduleItem.text = context.getString(
                        R.string._1_s,
                        absoluteAdapterPosition + 1,
                        scheduleData.title
                    )
                    itemView.animation = AnimationUtils.loadAnimation(
                        context,
                        R.anim.up_anim
                    )
                }

            }
        }
    }
}