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

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.qfxl.R
import com.qfxl.view.adapter.BannerAdapter
import com.qfxl.view.adapter.DefaultBannerAdapter
import com.qfxl.view.indicator.BannerIndicator

/**
 * <pre>
 *     author : qfxl
 *     e-mail : xuyonghong0822@gmail.com
 *     time   : 2018/07/16
 *     desc   : Banner
 *     version: 1.0
 * </pre>
 */
class Banner : FrameLayout {

    companion object {
        const val HORIZONTAL = RecyclerView.HORIZONTAL
        const val VERTICAL = RecyclerView.VERTICAL
    }

    /**
     * 轮播方向
     */
    var orientation = HORIZONTAL
        set(value) {
            field = value
            bannerRv.layoutManager = LinearLayoutManager(context, value, false)
        }
    /**
     * 设置layoutManeger
     */
    var layoutManager: LinearLayoutManager? = null
        set(value) {
            field = value
            bannerRv.layoutManager = field
        }
    /**
     * core
     */
    private val bannerRv: RecyclerView = RecyclerView(context)
    /**
     * 当前位置
     */
    var position = 0
    /**
     * 轮播时间
     */
    var loopInterval = 3000
    /**
     * 是否支持无限循环
     */
    var isInfinityEnabled = true
    /**
     * banner点击监听
     */
    private var onBannerClickListener: OnBannerClickListener? = null
    /**
     * banner状态监听
     */
    var onPageChangeListeners = ArrayList<OnPageChangedListener>()
        private set
    /**
     * 是否使用默认指示器
     */
    var isDefaultIndicator = true
    /**
     * 默认指示器大小
     */
    var indicatorItemSize = 0
    /**
     * 默认指示器item默认的资源
     */
    var indicatorNormalRes = 0
    /**
     * 默认指示器item选中的资源
     */
    var indicatorSelectRes = 0
    /**
     * 默认指示器的宽度
     */
    var indicatorWidth = 0
    /**
     * 默认指示器的高度
     */
    var indicatorHeight = 0
    /**
     * 指示器内部的gravity
     */
    var indicatorGravity = -1
    /**
     * 指示器的位置，left，top，right，bottom
     */
    var indicatorPosition = -1
    /**
     * 指示器
     */
    private val indicator = BannerIndicator(context)
    /**
     * 适配器
     */
    var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
        set(value) {
            if (value != null && value != bannerRv.adapter) {
                field = value
                val realAdapter = BannerAdapter(value)
                realAdapter.isInfinityEnabled = isInfinityEnabled
                realAdapter.onBannerClickListener = onBannerClickListener
                bannerRv.adapter = realAdapter
                //开启无限轮播则将位当前置居中
                if (isInfinityEnabled) {
                    position = realAdapter.itemCount / 2
                    bannerRv.scrollToPosition(position)
                }
                //处理指示器
                if (isDefaultIndicator) {
                    indicator.normalRes = indicatorNormalRes
                    indicator.selectedRes = indicatorSelectRes
                    indicator.indicatorItemSize = indicatorItemSize
                    indicator.indicatorWidth = indicatorWidth
                    indicator.indicatorHeight = indicatorHeight
                    indicator.indicatorPosition = indicatorPosition
                    indicator.gravity = indicatorGravity or if (orientation == HORIZONTAL) Gravity.CENTER_VERTICAL else Gravity.CENTER_HORIZONTAL
                    indicator.attachToBanner(this)
                }
            }
        }
    /**
     * 使用默认适配器下的数据源
     */
    var urlList: List<String> = ArrayList()
        set(value) {
            field = value
            if (!value.isEmpty()) {
                val defaultAdapter = DefaultBannerAdapter(value)
                defaultAdapter.imageLoader = imageLoader
                @Suppress("UNCHECKED_CAST")
                adapter = defaultAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
            }
        }
    /**
     * 轮播任务
     */
    private val loopTask = Runnable {
        if (!isInfinityEnabled && adapter != null) {
            if (position >= adapter!!.itemCount - 1) {
                position = -1
            }
        }
        bannerRv.smoothScrollToPosition(++position)
        onPageChangeListeners.forEach {
            it.onPageSelected(if (isInfinityEnabled) position % adapter!!.itemCount else position)
        }
        startLoop()
    }
    /**
     * 是否自动循环
     */
    var isAutoPlay = false
        set(value) {
            field = value
            if (value) {
                startLoop()
            } else {
                stopLoop()
            }
        }
    /**
     * 记录手指按下的x,y
     */
    private var startX = 0
    private var startY = 0

    /**
     * banner image loader
     */
    var imageLoader: IBannerImageLoader? = null

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleRes: Int) : super(context, attributeSet, defStyleRes) {
        getAttributes(context, attributeSet)
        bannerRv.layoutManager = LinearLayoutManager(context, orientation, false)
        BannerSnapHelper().apply {
            attachToRecyclerView(bannerRv)
            attachToBanner(this@Banner)
        }
        bannerRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onPageChangeListeners.forEach {
                    it.onPageScrollStateChanged(newState)
                }
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {

                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {

                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {

                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                onPageChangeListeners.forEach {
                    it.onPageScrolled(dx, dy)
                }
            }
        })
        addView(bannerRv)
    }

    /**
     * 获取自定义属性
     */
    private fun getAttributes(context: Context, attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.Banner)
        orientation = a.getInt(R.styleable.Banner_banner_orientation, HORIZONTAL)
        isAutoPlay = a.getBoolean(R.styleable.Banner_banner_autoPlay, true)
        loopInterval = a.getInt(R.styleable.Banner_banner_loopInterval, 3000)
        isInfinityEnabled = a.getBoolean(R.styleable.Banner_banner_isInfinityEnabled, true)
        isDefaultIndicator = a.getBoolean(R.styleable.Banner_banner_isDefaultIndicator, true)
        indicatorNormalRes = a.getResourceId(R.styleable.Banner_banner_indicatorNormalRes, R.drawable.sp_default_indicator_normal)
        indicatorSelectRes = a.getResourceId(R.styleable.Banner_banner_indicatorSelectRes, R.drawable.sp_default_indicator_selected)
        indicatorWidth = a.getDimensionPixelSize(R.styleable.Banner_banner_indicatorWidth, context.dpToPixel(20))
        indicatorHeight = a.getDimensionPixelSize(R.styleable.Banner_banner_indicatorHeight, context.dpToPixel(20))
        indicatorItemSize = a.getDimensionPixelSize(R.styleable.Banner_banner_indicatorSize,context.dpToPixel(6))
        val indicatorGravityInt = a.getInt(R.styleable.Banner_banner_indicatorGravity, -1)
        indicatorGravity = when (indicatorGravityInt) {
            0 -> Gravity.LEFT
            1 -> Gravity.TOP
            2 -> Gravity.RIGHT
            3 -> Gravity.BOTTOM
            else -> Gravity.CENTER
        }

        val indicatorPositionInt = a.getInt(R.styleable.Banner_banner_indicatorPosition, -1)
        indicatorPosition = when (indicatorPositionInt) {
            0 -> Gravity.LEFT
            1 -> Gravity.TOP
            2 -> Gravity.RIGHT
            else -> Gravity.BOTTOM

        }
        a.recycle()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopLoop()
    }

    override fun onVisibilityChanged(changedView: View?, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (isAutoPlay) {
            if (visibility == VISIBLE) {
                startLoop()
            } else {
                stopLoop()
            }
        }
    }

    /**
     * 开始循环
     */
    @Synchronized
    private fun startLoop() {
        stopLoop()
        postDelayed(loopTask, loopInterval.toLong())
    }

    /**
     * 停止循环
     */
    @Synchronized
    private fun stopLoop() {
        removeCallbacks(loopTask)
    }

    /**
     * add onPageChangeListener
     * @param onPageChangedListener listener
     */
    fun addOnPageChangeListener(onPageChangedListener: OnPageChangedListener) {
        synchronized(onPageChangeListeners, {
            onPageChangeListeners.add(onPageChangedListener)
        })
    }

    /**
     * remove onPageChangeListener
     * @param onPageChangedListener listener
     */
    fun removeOnPageChangeListener(onPageChangedListener: OnPageChangedListener) {
        synchronized(onPageChangeListeners, {
            onPageChangeListeners.remove(onPageChangedListener)
        })
    }

    /**
     * 设置banner点击监听
     */
    fun setBannerClickListener(action: (position: Int, v: View) -> Unit) {
        onBannerClickListener = object : OnBannerClickListener {
            override fun onBannerClick(position: Int, v: View) {
                action(position, v)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(true)
                stopLoop()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.x.toInt()
                val moveY = ev.y.toInt()
                val disX = moveX - startX
                val disY = moveY - startY
                parent.requestDisallowInterceptTouchEvent(2 * Math.abs(disX) > Math.abs(disY))
                stopLoop()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isAutoPlay) {
                    startLoop()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 移除默认的指示器
     */
    fun removeDefaultIndicator() {
        if (isDefaultIndicator) {
            indicator.detachFromBanner()
        }
    }
}

interface OnBannerClickListener {
    /**
     * @param position banner's position
     * @param v banner item
     */
    fun onBannerClick(position: Int, v: View)
}

interface OnPageChangedListener {
    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     * @param dx dx
     * @param dy dy
     */
    fun onPageScrolled(dx: Int, dy: Int) {}

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    fun onPageSelected(position: Int) {}

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see RecyclerView#SCROLL_STATE_IDLE
     * @see RecyclerView#SCROLL_STATE_DRAGGING
     * @see RecyclerView#SCROLL_STATE_SETTLING
     */
    fun onPageScrollStateChanged(state: Int) {}
}