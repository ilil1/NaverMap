package com.project.navermap.extensions

import android.content.res.Resources
//float형태를 dp를 픽셀로
fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}
