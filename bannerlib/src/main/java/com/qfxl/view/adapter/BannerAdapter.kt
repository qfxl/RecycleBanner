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
import android.view.ViewGroup
import com.qfxl.view.OnBannerClickListener

/**
 * <pre>
 *     author : qfxl
 *     e-mail : 3001567284@qq.com
 *     time   : 2018/07/19
 *     desc   : Banner适配器
 *     version: 1.0
 * </pre>
 */
class BannerAdapter(private val realAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 返回数量的倍数，如果数量 > 2，则返回的数量为 count*ratio
     */
    private val ratio = 500

    /**
     * 是否开启循环
     */
    var isInfinityEnabled = false
    /**
     * banner点击监听
     */
    var onBannerClickListener: OnBannerClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = realAdapter.onCreateViewHolder(parent, viewType)

    override fun getItemCount(): Int = if (realAdapter.itemCount > 1 && isInfinityEnabled) realAdapter.itemCount * ratio else realAdapter.itemCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val originPosition = if (isInfinityEnabled) position % realAdapter.itemCount else position
        realAdapter.onBindViewHolder(holder, originPosition)
        holder.itemView.setOnClickListener {
            onBannerClickListener?.onBannerClick(originPosition, it)
        }
    }
}