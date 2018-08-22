package com.qfxl.banner

import android.app.Activity
import android.content.Intent

fun Activity.navigation(cls: Class<out Any>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}