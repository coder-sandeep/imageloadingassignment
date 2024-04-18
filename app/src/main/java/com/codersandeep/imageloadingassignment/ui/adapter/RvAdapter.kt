package com.codersandeep.imageloadingassignment.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.codersandeep.imageloadingassignment.R
import com.codersandeep.imageloadingassignment.utils.cache.BitmapCacheHandler
import com.codersandeep.imageloadingassignment.models.ArticlesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RvAdapter(var article: ArticlesResponse? = null, context: Context) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    val bitmapCacheHandler = BitmapCacheHandler(context)

    // holder class to hold reference
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get view reference
        var thumbnail: AppCompatImageView = view.findViewById(R.id.ivThumbnail)
        var imageCount: TextView = view.findViewById(R.id.tvImageCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //set values
        val thumbnail = article?.get(position)?.thumbnail
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = bitmapCacheHandler.getImage(
                "${thumbnail?.domain}/${thumbnail?.basePath}/${
                    thumbnail?.qualities?.get(
                        0
                    )
                }/${thumbnail?.key}", thumbnail?.id ?: ""
            )
            withContext(Dispatchers.Main) {
                holder.thumbnail.setImageBitmap(bitmap)
            }
        }
        holder.imageCount.text = "${position + 1}"
    }

    override fun getItemCount(): Int {
        return article?.size ?: 0
    }

    //     update your data
    fun updateData(articleList: ArticlesResponse) {
        article = articleList
    }
}
