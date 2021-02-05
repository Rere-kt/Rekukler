package com.rerekt.rekukler.dsl

import android.graphics.Bitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.rerekt.rekukler.RecyclerViewConfig

fun RecyclerViewConfig.dividerItemDecoration(
    orientation: Int = DividerItemDecoration.VERTICAL,
    block: DividerItemDecoration.() -> Unit
) {
    itemDecoration(
        DividerItemDecoration(context, orientation).apply(block)
    )
}

fun RecyclerViewConfig.dividerItemDecoration(
    size: Int,
    orientation: Int = DividerItemDecoration.VERTICAL
) {
    itemDecoration(
        DividerItemDecoration(context, orientation).apply {
            setDrawable(
                Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
                    .toDrawable(context.resources)
            )
        }
    )
}

fun RecyclerViewConfig.itemDecoration(decoration: RecyclerView.ItemDecoration) {
    itemDecorations.add(decoration)
}