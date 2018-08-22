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
package com.qfxl.view.layoutmanager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.DisplayMetrics
import com.qfxl.view.Banner
import com.qfxl.view.BannerSnapHelper

/**
 * <pre>
 *     author : qfxl
 *     e-mail : xuyonghong0822@gmail.com
 *     time   : 2018/08/21
 *     desc   : ViewPagerLayoutManager
 *     version: 1.0
 * </pre>
 */
class ViewPagerLayoutManager : LinearLayoutManager {
    /**
     * 滑过1px时经历的时间(ms)的系数,值越大滑动速率越慢
     */
    var scrollTimeDelayRatio = 2.5F

    private val snapHelper = BannerSnapHelper()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * attachBanner
     * @param banner banner
     */
    fun attachToBanner(banner: Banner) {
        snapHelper.attachToBanner(banner)
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        snapHelper.attachToRecyclerView(view)
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val linearSmoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                //滑过1px时经历的时间(ms),值越大滑动速率越慢
                return super.calculateSpeedPerPixel(displayMetrics) * scrollTimeDelayRatio
            }
        }
        linearSmoothScroller.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }
}