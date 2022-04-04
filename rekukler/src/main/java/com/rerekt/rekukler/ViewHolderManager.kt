package com.rerekt.rekukler

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

interface ViewHolderManager {
    val itemView: View

    fun getString(@StringRes resId: Int): String = itemView.context.getString(resId)
    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = itemView.context.getString(resId, *formatArgs)
    fun getDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(itemView.context, resId)
    fun getColor(@ColorRes resId: Int): Int = itemView.context.getColor(resId)
    fun getColorStateList(@ColorRes resId: Int): ColorStateList? = ContextCompat.getColorStateList(itemView.context, resId)
    fun <V: View> findViewById(@IdRes resId: Int): V = itemView.findViewById(resId)
    fun setOnClickListener(l: (View) -> Unit) {
        itemView.setOnClickListener { l.invoke(it) }
    }
}