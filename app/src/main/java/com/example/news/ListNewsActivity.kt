package com.example.news

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.Adapter.ViewHolder.ListNewsAdapter
import com.example.news.Common.Common
import com.example.news.Interface.NewsService
import com.example.news.Model.News
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_list_news.*
import kotlinx.android.synthetic.main.activity_main.swipe_to_refresh
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListNewsActivity : AppCompatActivity() {

    var source = ""
    var webHotUrl: String? = ""

    private lateinit var dialog: AlertDialog
    private lateinit var mService: NewsService
    private lateinit var adapter: ListNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)

        //init view
        mService = Common.newsService
        dialog = SpotsDialog(this)

        swipe_to_refresh.setOnRefreshListener { loadNews(source, true) }

        frame_layout.setOnClickListener {
            val detail = Intent(baseContext, NewsDetail::class.java)
            detail.putExtra("webURL", webHotUrl)
            startActivity(detail)
        }

        list_news.setHasFixedSize(true)
        list_news.layoutManager = LinearLayoutManager(this)

        if (intent != null) {
            source = intent.getStringExtra("source")!!
            if (!source.isEmpty()) {
                loadNews(source, false)
            }

        }
    }

    private fun loadNews(source: String?, isRefresh: Boolean) {
        if (isRefresh) {
            dialog.show()
            mService.getNewsFromSource(Common.getNewsAPI(source!!))
                .enqueue(object : Callback<News> {
                    override fun onResponse(call: Call<News>, response: Response<News>) {
                        dialog.dismiss()

                        //get first article to hot news
                        Picasso.with(baseContext)
                            .load(response.body()!!.articles!![0].urlToImage)
                            .into(top_image)

                        top_title.text = response.body()!!.articles!![0].title
                        top_author.text = response.body()!!.articles!![0].author

                        webHotUrl = response.body()!!.articles!![0].url

                        //load all remain articles
                        val removeFirstItem = response.body()!!.articles

                        //because we get first item to hot news, so we need removw it
                        removeFirstItem!!.removeAt(0)

                        adapter = ListNewsAdapter(removeFirstItem, baseContext)
                        adapter.notifyDataSetChanged()
                        list_news.adapter = adapter
                    }

                    override fun onFailure(call: Call<News>, t: Throwable) {
                        TODO("Not yet implemented")
                    }


                })
        } else {
            swipe_to_refresh.isRefreshing = true

            mService.getNewsFromSource(Common.getNewsAPI(source!!))
                .enqueue(object : Callback<News> {
                    override fun onResponse(call: Call<News>, response: Response<News>) {

                        swipe_to_refresh.isRefreshing = false

                        //get first article to hot news
                        Picasso.with(baseContext)
                            .load(response.body()!!.articles!![0].urlToImage)
                            .into(top_image)

                        top_title.text = response.body()!!.articles!![0].title
                        top_author.text = response.body()!!.articles!![0].author

                        webHotUrl = response.body()!!.articles!![0].url

                        //load all remain articles
                        val removeFirstItem = response.body()!!.articles

                        //because we get first item to hot news, so we need removw it
                        removeFirstItem!!.removeAt(0)

                        adapter = ListNewsAdapter(removeFirstItem, baseContext)
                        adapter.notifyDataSetChanged()
                        list_news.adapter = adapter
                    }

                    override fun onFailure(call: Call<News>, t: Throwable) {
                        TODO("Not yet implemented")
                    }


                })
        }

    }
}