package com.qfxl.banner.binder

import android.support.annotation.IdRes
@Retention(AnnotationRetention.RUNTIME) //保存在内存中的字节码，JVM将在运行时也保留注解，因此可以通过反射机制读取注解的信息
@Target(AnnotationTarget.FIELD)//作用于成员变量、对象、属性（包括enum实例）
annotation class BindView(@IdRes val value: Int)