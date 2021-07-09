package com.example.news

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.Adapter.ViewHolder.ListSourceAdapter
import com.example.news.Common.Common
import com.example.news.Interface.NewsService
import com.example.news.Model.Website
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var mService: NewsService
    lateinit var adapter: ListSourceAdapter
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init cache OS
        Paper.init(this)

        //Init Service
        mService = Common.newsService

        //Init View
        swipe_to_refresh.setOnRefreshListener {
            loadWebsiteSource(true)
        }

        recyclerview_source_news.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerview_source_news.layoutManager = layoutManager

        dialog = SpotsDialog(this)

        loadWebsiteSource(false)


    }

    private fun loadWebsiteSource(isRefresh: Boolean) {
        if (!isRefresh) {
            val cache = Paper.book().read<String>("cache")
            if (cache != null && !cache.isBlank() && cache != "null") {
                //Read cache
                val webSite = Gson().fromJson<Website>(cache, Website::class.java)
                adapter = ListSourceAdapter(baseContext, webSite)
                adapter.notifyDataSetChanged()
                recyclerview_source_news.adapter = adapter
            } else {
                //Load website and write cache
                dialog.show()
                //Fetch new data
                mService.sources.enqueue(object : Callback<Website> {
                    override fun onResponse(call: Call<Website>, response: Response<Website>) {
                        adapter = ListSourceAdapter(baseContext, response!!.body()!!)
                        adapter.notifyDataSetChanged()
                        recyclerview_source_news.adapter = adapter

                        //Save to cache
                        Paper.book().write("cahe", Gson().toJson(response.body()))

                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<Website>, t: Throwable) {
                        Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            swipe_to_refresh.isRefreshing = true
            //Fetch new data
            mService.sources.enqueue(object : Callback<Website> {
                override fun onResponse(call: Call<Website>, response: Response<Website>) {
                    adapter = ListSourceAdapter(baseContext, response!!.body()!!)
                    adapter.notifyDataSetChanged()
                    recyclerview_source_news.adapter = adapter

                    //Save to cache
                    Paper.book().write("cahe", Gson().toJson(response.body()))

                    swipe_to_refresh.isRefreshing = false
                }

                override fun onFailure(call: Call<Website>, t: Throwable) {
                    Toast.makeText(baseContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}