package com.extcode.project.mytomato.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.extcode.project.mytomato.R
import com.extcode.project.mytomato.adapter.NewsAdapter
import com.extcode.project.mytomato.data.response.Items
import com.extcode.project.mytomato.viewModel.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val sharedPref = getSharedPreferences(
            NameActivity::class.simpleName,
            Context.MODE_PRIVATE
        )

        val interested = sharedPref.getString("interested", null)

        configRecyclerView()
        configNewsViewModel(interested)

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun configRecyclerView() {

        newsAdapter = NewsAdapter()
        newsAdapter.notifyDataSetChanged()

        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.adapter = newsAdapter

        newsAdapter.setOnItemClickCallback(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Items) {
                val url = items.url
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
            }
        })

    }

    private fun configNewsViewModel(category: String?) {

        showProgressBar(true)
        showNotFound(false)
        newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        newsViewModel.setNewsCategory(category)
        newsViewModel.getNewsCategory().observe(this, Observer {

            try {
                if (it != null) {
                    showProgressBar(false)
                    showNotFound(false)
                    newsAdapter.arrNews = it
                } else {
                    showProgressBar(false)
                    showNotFound(true)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to get News", Toast.LENGTH_SHORT).show()
                showProgressBar(false)
                showNotFound(true)
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