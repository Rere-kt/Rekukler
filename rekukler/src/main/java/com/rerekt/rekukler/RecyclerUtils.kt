package com.rerekt.rekukler

import java.util.*

//Function for clip the borders
internal fun clip(size: Int, start: Int, end: Int, delta: Float): Float {
    val newStart = start + delta
    val newEnd = end + delta

    val outOfBorderStart = 0 - newStart
    val outOfBorderEnd= newEnd - size

    return when {
        outOfBorderStart > 0 -> delta + outOfBorderStart
        outOfBorderEnd > 0 -> delta - outOfBorderEnd
        else -> delta
    }
}