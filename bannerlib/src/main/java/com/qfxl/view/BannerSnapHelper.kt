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
package com.qfxl.view

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView

/**
 * <pre>
 *     author : qfxl
 *     e-mail : xuyonghong0822@gmail.com
 *     time   : 2018/07/17
 *     desc   : BannerSnapHelper
 *     version: 1.0
 * </pre>
 */
class BannerSnapHelper : PagerSnapHelper() {
    private lateinit var mBanner: Banner
    /**
     * attachBanner
     * @param banner banner
     */
    fun attachToBanner(banner: Banner) {
        mBanner = banner
    }

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        val targetPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        mBanner.position = targetPosition
        val realPosition = if (mBanner.isInfinityEnabled) targetPosition % mBanner.adapter!!.itemCount else targetPosition
        mBanner.onPageChangeListeners.forEach {
            if (mBanner.isInfinityEnabled) {
                it.onPageSelected(realPosition)
            } else {
                if(targetPosition < mBanner.adapter!!.itemCount) {
                    it.onPageSelected(realPosition)
                }
            }

        }
        return targetPosition
    }
}