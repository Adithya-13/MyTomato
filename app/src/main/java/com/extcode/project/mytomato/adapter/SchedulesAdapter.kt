package com.extcode.project.mytomato.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.ScheduleData
import kotlinx.android.synthetic.main.item_recyclerview_schedules.view.*

class SchedulesAdapter : RecyclerView.Adapter<SchedulesAdapter.ViewHolder>() {

    var arrSchedule = ArrayList<ScheduleData>()
        set(value) {
            this.arrSchedule.clear()
            this.arrSchedule.addAll(value)
            notifyDataSetChanged()
        }

    private var onDeleteItem: OnDeleteItem? = null

    fun setOnDeleteItem(onDeleteItem: OnDeleteItem) {
        this.onDeleteItem = onDeleteItem
    }

    interface OnDeleteItem {
        fun deleteItem(isRemoved: Boolean)
    }

    fun removeItem(position: Int) {
        this.arrSchedule.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recyclerview_schedules, parent, false)
        )
    }

    override fun getItemCount(): Int = arrSchedule.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrSchedule[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(scheduleData: ScheduleData) {
            with(itemView) {

                deleteSchedule.setOnClickListener {
                    if (schedule.hasFocus()) {
                        schedule.clearFocus()
                        val imm: InputMethodManager =
                            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(windowToken, 0)
                    }
                    removeItem(absoluteAdapterPosition)
                    onDeleteItem?.deleteItem(true)
                }

                schedule.editText?.setText(scheduleData.title)
                schedule.editText?.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        arrSchedule[absoluteAdapterPosition].title =
                            schedule.editText?.text.toString()

                    }
                })

                itemView.animation = AnimationUtils.loadAnimation(
                    itemView.context,
                    R.anim.up_anim
                )

            }
        }
    }
}