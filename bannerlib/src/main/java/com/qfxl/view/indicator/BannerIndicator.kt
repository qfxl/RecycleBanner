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
package com.qfxl.view.indicator

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.qfxl.R
import com.qfxl.view.Banner
import com.qfxl.view.OnPageChangedListener
import com.qfxl.view.dpToPixel
import com.qfxl.view.forEachIndexed

/**
 * <pre>
 *     author : qfxl
 *     e-mail : xuyonghong0822@gmail.com
 *     time   : 2018/07/17
 *     desc   : BannerIndicator
 *     version: 1.0
 * </pre>
 */
class BannerIndicator(context: Context) : LinearLayout(context) {
    /**
     * banner
     */
    private lateinit var mBanner: Banner
    /**
     * indicator的数量
     */
    private var indicatorCount = 0
    /**
     * 指示器选中的资源
     */
    var selectedRes = R.drawable.sp_default_indicator_selected
    /**
     * 指示器正常资源
     */
    var normalRes = R.drawable.sp_default_indicator_normal
    /**
     * indicator item之间得间距
     */
    var itemSpace = context.dpToPixel(5)
    /**
     * 默认指示器的宽度，针对orientation=VERTICAL的情况
     */
    var indicatorWidth = 0
    /**
     * 默认指示器的高度，针对orientation=HORIZONTAL的情况
     */
    var indicatorHeight = 0
    /**
     * 指示器的位置left,top,right,bottom
     */
    var indicatorPosition = 0
    /**
     * 指示器的大小（默认大小一致）
     */
    var indicatorItemSize = 0

    /**
     * attachBanner
     * @param banner Banner
     */
    fun attachToBanner(banner: Banner, lp: FrameLayout.LayoutParams? = null) {
        mBanner = banner
        detachFromBanner()
        orientation = banner.orientation
        createIndicators()
        if (lp == null) {
            when (orientation) {
                HORIZONTAL -> {
                    val horizontalLp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, indicatorHeight)
                    horizontalLp.gravity = indicatorPosition
                    mBanner.addView(this, horizontalLp)
                }
                else -> {
                    val verticalLp = FrameLayout.LayoutParams(indicatorWidth, FrameLayout.LayoutParams.MATCH_PARENT)
                    verticalLp.gravity = indicatorPosition
                    mBanner.addView(this, verticalLp)
                }
            }
        } else {
            mBanner.addView(this, lp)
        }
        mBanner.addOnPageChangeListener(object : OnPageChangedListener {
            override fun onPageSelected(position: Int) {
                forEachIndexed({ index, view ->
                    if (position == index) {
                        (view as ImageView).setImageResource(selectedRes)
                    } else {
                        (view as ImageView).setImageResource(normalRes)
                    }
                })
            }
        })
    }

    /**
     * detachFromBanner
     */
    fun detachFromBanner() {
        if (::mBanner.isInitialized) {
            removeAllViews()
            mBanner.removeView(this)
        }

    }

    /**
     * 创建indicators
     */
    private fun createIndicators() {
        if (::mBanner.isInitialized) {
            removeAllViews()
            indicatorCount = if (mBanner.adapter!!.itemCount > 1) mBanner.adapter!!.itemCount else 0
            if (indicatorCount > 0) {
                val lp = LayoutParams(indicatorItemSize, indicatorItemSize)
                when (orientation) {
                    HORIZONTAL -> {
                        lp.leftMargin = itemSpace
                    }
                    VERTICAL -> {
                        lp.topMargin = itemSpace
                    }
                }
                for (i in 0 until indicatorCount) {
                    val indicator = ImageView(context)
                    if (i == 0) {
                        indicator.setImageResource(selectedRes)
                    } else {
                        indicator.setImageResource(normalRes)
                    }
                    if (i < indicatorCount - 1) {
                        indicator.layoutParams = lp
                    } else {
                        //处理最后一个item
                        val lastLp = LayoutParams(indicatorItemSize, indicatorItemSize)
                        when (orientation) {
                            HORIZONTAL -> {
                                lastLp.leftMargin = itemSpace
                                lastLp.rightMargin = itemSpace
                            }
                            VERTICAL -> {
                                lastLp.topMargin = itemSpace
                                lastLp.bottomMargin = itemSpace
                            }
                        }
                        indicator.layoutParams = lastLp
                    }
                    addView(indicator)
                }
            }
        }
    }
}