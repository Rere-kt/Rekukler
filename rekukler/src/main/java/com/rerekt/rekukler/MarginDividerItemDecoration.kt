package com.rerekt.rekukler

import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class MarginDividerItemDecoration constructor(
    private val orientation: Int = VERTICAL,
    private var dividerLineMargin: Float = 0f,
    @ColorRes private var dividerLineColorRes: Int = android.R.color.black,
    decorationSize: Int = 0,
    dividerLineWidth: Int = 0
): ItemDecoration() {

    private var decoratorSize: Int = decorationSize
    private var dividerLineWidth: Float = dividerLineWidth.toFloat()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (orientation == HORIZONTAL)
            drawHorizontal(canvas, parent)
        else
            drawVertical(canvas, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val top = (child.bottom + decoratorSize / 2).toFloat()
            val bottom = top
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

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            val left = (child.right + decoratorSize / 2).toFloat()
            val right = left
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

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            if (orientation == HORIZONTAL)
                outRect.right = decoratorSize
            else
                outRect.bottom = decoratorSize
        }
    }

    companion object {
        private const val HORIZONTAL = LinearLayout.HORIZONTAL
        private const val VERTICAL = LinearLayout.VERTICAL
    }

}