package com.rerekt.rekukler.dsl

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rerekt.rekukler.MultiBindingAdapter
import com.rerekt.rekukler.RecyclerViewConfig
import com.rerekt.rekukler.utils.clip

fun RecyclerViewConfig.itemTouchHelper(
    dragFlags: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT,
    swipeFlags: Int = 0,
    onSwiped: (RecyclerView.ViewHolder, direction: Int) -> Unit = { _, _ -> },
    onClearView: () -> Unit = {},
    isCanBeOutOfBounds: Boolean = false,
    onMove: ((RecyclerView.ViewHolder, RecyclerView.ViewHolder) -> Boolean)? = null
) = ItemTouchHelper(
    object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int = makeMovementFlags(dragFlags, swipeFlags)
        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) = onSwiped.invoke(viewHolder, direction)
        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) = onClearView.invoke()
        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if (isCanBeOutOfBounds) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            } else {
                val clippedDx  = clip(recyclerView.width, viewHolder.itemView.left, viewHolder.itemView.right, dX)
                val clippedDy  = clip(recyclerView.height, viewHolder.itemView.top, viewHolder.itemView.bottom, dY)
                super.onChildDraw(c, recyclerView, viewHolder, clippedDx, clippedDy, actionState, isCurrentlyActive)
            }
        }
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = onMove?.invoke(viewHolder, target) ?: (recyclerView.adapter as? MultiBindingAdapter)?.moveItem(viewHolder.adapterPosition, target.adapterPosition) ?: false
    }
).apply { itemTouchHelper = this }