package com.extcode.project.mytomato.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.data.response.Items
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder>() {

    var arrNews = ArrayList<Items>()
        set(value) {
            this.arrNews.clear()
            this.arrNews.addAll(value)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(items: Items)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterViewHolder {
        return NewsAdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun getItemCount(): Int = arrNews.size

    override fun onBindViewHolder(holder: NewsAdapterViewHolder, position: Int) {
        holder.bind(arrNews[position])
    }

    inner class NewsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(items: Items) {
            with(itemView) {

                srcName.text = items.source.name
                srcTitle.text = items.title
                Glide.with(itemView.context)
                    .load(items.urlToImage)
                    .into(urlToImage)

                itemView.animation = AnimationUtils.loadAnimation(itemView.context, R.anim.up_anim)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(items) }

            }
        }
    }
}