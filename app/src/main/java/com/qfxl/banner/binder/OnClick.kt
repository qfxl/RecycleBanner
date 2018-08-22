package com.qfxl.banner.binder

import android.support.annotation.IdRes

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class OnClick(@IdRes val value: Int)