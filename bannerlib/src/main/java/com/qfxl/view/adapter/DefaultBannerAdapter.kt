package com.qfxl.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.qfxl.R
import com.qfxl.view.IBannerImageLoader

/**
 * <pre>
 *     author : qfxl
 *     e-mail : 3001567284@qq.com
 *     time   : 2018/07/19
 *     desc   : default banner adapter
 *     version: 1.0
 * </pre>
 */
class DefaultBannerAdapter(private val urlList: List<String>) : RecyclerView.Adapter<SimpleViewHolder>() {
    var imageLoader: IBannerImageLoader? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_banner, parent, false)
        return SimpleViewHolder(itemView)
    }

    override fun getItemCount(): Int = urlList.size

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        imageLoader?.displayImage(holder.itemView.context, urlList[position], holder.bannerItemView)
    }
}

class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val bannerItemView: ImageView = itemView.findViewById(R.id.iv_banner_item)
}