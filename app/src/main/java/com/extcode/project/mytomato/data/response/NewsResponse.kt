package com.extcode.project.mytomato.data.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @SerializedName("articles")
    val items: ArrayList<Items>

)

data class Items(

    @SerializedName("source")
    val source: Source,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("urlToImage")
    val urlToImage: String
)

data class Source(
    @SerializedName("name")
    val name: String
)