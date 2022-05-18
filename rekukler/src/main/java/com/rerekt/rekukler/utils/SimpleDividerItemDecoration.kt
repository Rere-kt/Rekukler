package com.rerekt.rekukler.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Rect
import android.graphics.Color
import android.graphics.Canvas
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SimpleDividerItemDecoration(builder: Builder): ItemDecoration() {

    private val orientation: Int = builder.orientation
    private val margin: Float = builder.margin
    private val size: Int = builder.size
    private val dividerPaint = builder.dividerPaint
    private val decorationPaint = builder.decorationPaint

    private val fullSize: Float = size + dividerPaint.strokeWidth
    private val dividerRect = RectF()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.childCount == 0) return
        when(orientation) {
            RecyclerView.VERTICAL -> drawVertical(canvas, parent)
            RecyclerView.HORIZONTAL -> drawHorizontal(canvas, parent)
            else -> IllegalArgumentException("Unsupported orientation: $orientation")
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft.toFloat()
        val right = parent.width.toFloat() - parent.paddingRight
        val holders = parent.children.take(parent.childCount - 1)

        holders.forEach { holder ->
            val top = holder.bottom.toFloat()
            val bottom = top + fullSize
            dividerRect.set(
                left,
                top,
                right,
                bottom
            )

            canvas.drawRect(
                dividerRect,
                decorationPaint
            )

            if (dividerPaint.strokeWidth != 0f) {
                canvas.drawLine(
                    dividerRect.left + margin,
                    dividerRect.centerY(),
                    dividerRect.right - margin,
                    dividerRect.centerY(),
                    dividerPaint
                )
            }
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop.toFloat()
        val bottom = parent.height.toFloat() - parent.paddingBottom
        val holders = parent.children.take(parent.childCount - 1)

        holders.forEach { holder ->
            val right = holder.right.toFloat()
            val left = right + fullSize
            dividerRect.set(
                left,
                top,
                right,
                bottom
            )

            canvas.drawRect(
                dividerRect,
                decorationPaint
            )

            if (dividerPaint.strokeWidth != 0f) {
                canvas.drawLine(
                    dividerRect.centerX(),
                    dividerRect.top + margin,
                    dividerRect.centerX(),
                    dividerRect.bottom - margin,
                    dividerPaint
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
            if (orientation == RecyclerView.VERTICAL)
                outRect.bottom = fullSize.toInt()
            else
                outRect.right = fullSize.toInt()
        }
    }

    class Builder(private val context: Context) {

        var orientation: Int = RecyclerView.VERTICAL
        var margin: Float = 0f
        var size: Int = 0
        internal val dividerPaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.parseColor(COLOR_TRANSPARENT_HEX)
        }
        internal val decorationPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.parseColor(COLOR_TRANSPARENT_HEX)
        }

        fun setOrientation(orientation: Int): Builder {
            this.orientation = orientation
            return this
        }

        fun setColor(@ColorRes colorRes: Int): Builder {
            dividerPaint.color = ContextCompat.getColor(
                context,
                colorRes
            )
            return this
        }

        fun setMargin(margin: Float): Builder {
            this.margin = margin
            return this
        }

        fun setSize(size: Int): Builder {
            this.size = size
            return this
        }

        fun setDividerWidth(width: Float): Builder {
            dividerPaint.strokeWidth = width
            return this
        }

        fun applyDividerStyle(properties: Paint.() -> Unit): Builder {
            dividerPaint.apply(properties)
            return this
        }

        fun setDecorationBackgroundColorRes(@ColorRes colorRes: Int): Builder {
            decorationPaint.color = ContextCompat.getColor(
                context,
                colorRes
            )
            return this
        }

        fun setDecorationBackgroundColor(@ColorInt color: Int): Builder {
            decorationPaint.color = color
            return this
        }

        fun build() = SimpleDividerItemDecoration(this)
    }

    private companion object {
        const val COLOR_TRANSPARENT_HEX = "#00000000"
    }

}