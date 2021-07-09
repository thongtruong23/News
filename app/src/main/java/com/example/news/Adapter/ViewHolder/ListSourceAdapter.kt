package com.example.news.Adapter.ViewHolder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news.Interface.ItemClickListener
import com.example.news.ListNewsActivity
import com.example.news.Model.Website
import com.example.news.R

class ListSourceAdapter(private val context: Context, private val website: Website) :
    RecyclerView.Adapter<ListSoureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSoureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.source_news_layout, parent, false)
        return ListSoureViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return website.sources!!.size
    }

    override fun onBindViewHolder(holder: ListSoureViewHolder, position: Int) {
        holder.source_title.text = website.sources!![position].name

        holder.setItemClickListener(object : ItemClickListener {
            //Ctrl + O
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, ListNewsActivity::class.java)
                intent.putExtra("source", website.sources!![position].id)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        })
    }


}