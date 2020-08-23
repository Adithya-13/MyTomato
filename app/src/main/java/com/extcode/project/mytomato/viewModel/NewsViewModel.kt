package com.extcode.project.mytomato.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.extcode.project.mytomato.data.response.Items
import com.extcode.project.mytomato.data.response.NewsResponse
import com.extcode.project.mytomato.service.DataSource
import com.extcode.project.mytomato.service.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private val listNews = MutableLiveData<ArrayList<Items>>()

    fun setNewsCategory(category: String?) {

        val dataSource = NetworkProvider.providesHttpAdapter().create(DataSource::class.java)
        dataSource.userNews(category).enqueue(object : Callback<NewsResponse> {

            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                val item = response.body()?.items
                listNews.postValue(item)
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("tag", "onFailure: $t")
            }
        })
    }

    fun getNewsCategory(): LiveData<ArrayList<Items>> {
        return listNews
    }

}