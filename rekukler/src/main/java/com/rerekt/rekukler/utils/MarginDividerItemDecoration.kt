package com.rerekt.rekukler.utils

import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class MarginDividerItemDecoration constructor(
    private val orientation: Int = LinearLayout.VERTICAL,
    private val dividerLineMargin: Float = 0f,
    @ColorRes private val dividerLineColorRes: Int = android.R.color.black,
    private val dividerLineWidth: Float = 0f,
    decorationSize: Int = 0
): ItemDecoration() {

    private val decorationFullSize: Float = decorationSize + dividerLineWidth

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == LinearLayout.VERTICAL)
            drawVertical(canvas, parent)
        else
            drawHorizontal(canvas, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        if (childCount == 0) return
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val top = child.bottom + decorationFullSize / 2
            val bottom = top
            if (dividerLineWidth != 0f) {
                canvas.drawLine(
                    left + dividerLineMargin,
                    top,
                    right - dividerLineMargin,
                    bottom,
                    Paint().apply {
                        color = ContextCompat.getColor(parent.context, dividerLineColorRes)
                        strokeWidth = dividerLineWidth
                    }
                )
            }
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        if (childCount == 0) return
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val left = child.right + decorationFullSize / 2
            val right = left
            if (dividerLineWidth != 0f) {
                canvas.drawLine(
                    left,
                    top + dividerLineMargin,
                    right,
                    bottom - dividerLineMargin,
                    Paint().apply {
                        color = ContextCompat.getColor(parent.context, dividerLineColorRes)
                        strokeWidth = dividerLineWidth
                    }
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            if (orientation == LinearLayout.VERTICAL)
                outRect.bottom = decorationFullSize.toInt()
            else
                outRect.right = decorationFullSize.toInt()
        }
    }

}