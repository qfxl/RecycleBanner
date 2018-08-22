package com.qfxl.banner.binder

import android.app.Activity
import android.view.View
//https://blog.csdn.net/xuyonghong1122/article/details/80347201
object ViewBinder {
    fun bind(activity: Activity) {
        val clazz = activity.javaClass
        val fields = clazz.declaredFields
        fields.forEach {
            //判断注解是否存在
            if (it.isAnnotationPresent(BindView::class.java)) {
                //获取注解
                val viewId = it.getAnnotation(BindView::class.java).value
                val findView = clazz.getMethod("findViewById", Int::class.java)
                val view = findView.invoke(activity, viewId)
                it.isAccessible = true
                //赋值给注解标注的对象
                it.set(activity, view)
            }
        }

        val methods = clazz.declaredMethods
        methods.forEach { method ->
            if (method.isAnnotationPresent(OnClick::class.java)) {
                val viewId = method.getAnnotation(OnClick::class.java).value
                val findView = clazz.getMethod("findViewById", Int::class.java)
                val view = findView.invoke(activity, viewId)
                if (view != null) {
                    method.isAccessible = true
                    (view as View).setOnClickListener {
                        val types = method.genericParameterTypes
                        //判断方法参数长度
                        when (types.size) {
                            0 -> method.invoke(activity)
                            1 -> method.invoke(activity, it)
                        }
                    }
                }
            }
        }
    }
}