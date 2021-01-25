package com.rerekt.rekukler

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.updateLayoutParams

class AsyncLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
) {

    private var isInflated = false
    private var pendingActions: MutableList<AsyncLayout.() -> Unit> = ArrayList()

    fun inflateAsync(
        @LayoutRes layoutResId: Int
    ) {
        AsyncLayoutInflater(context).inflate(layoutResId, this) { view, _, _ ->
            addView(view)
            updateLayoutParams {
                width = view.layoutParams.width
                height = view.layoutParams.height
            }
            isInflated = true
            pendingActions.forEach { action -> action() }
            pendingActions.clear()
        }
    }

    fun invokeWhenInflated(action: AsyncLayout.() -> Unit) {
        if (isInflated) {
            action()
        } else {
            pendingActions.add(action)
        }
    }
}