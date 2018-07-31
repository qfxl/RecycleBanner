package com.qfxl.view

import android.content.Context
import android.view.View
import android.widget.ImageView

/**
 * <pre>
 *     author : qfxl
 *     e-mail : 3001567284@qq.com
 *     time   : 2018/07/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IBannerImageLoader {
    /**
     * display imageView
     * @param context
     * @param path url or local
     * @param imageView
     */
    fun displayImage(context: Context, path: Any, imageView: ImageView)
}