/*
 * Copyright (c) 2018 Frank Xu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 *     e-mail : xuyonghong0822@gmail.com
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