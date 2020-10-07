package com.rerekt.sample.ui.global

import android.content.res.Resources
import android.util.TypedValue

fun Int.dip(resource: Resources) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        resource.displayMetrics
    )