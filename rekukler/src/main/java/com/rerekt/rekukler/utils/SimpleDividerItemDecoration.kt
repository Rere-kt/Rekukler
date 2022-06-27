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
    private val marginStart: Float = builder.marginStart
    private val marginEnd: Float = builder.marginEnd
    private val size: Int = builder.size
    private val dividerPaint: Paint = builder.dividerPaint
    private val decorationPaint: Paint = builder.decorationPaint
    private val shouldDecorateLastItem: Boolean = builder.shouldDecorateLastItem

    private val decorationFullSize: Float = size + dividerPaint.strokeWidth
    private val dividerRect: RectF = RectF()

    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.childCount == 0) return

        val holders = if (shouldDecorateLastItem) {
            parent.children
        } else {
            parent.children.take(parent.childCount - 1)
        }

        when(orientation) {
            RecyclerView.VERTICAL -> drawVertical(
                canvas = canvas,
                parent = parent,
                viewHolders = holders
            )
            RecyclerView.HORIZONTAL -> drawHorizontal(
                canvas = canvas,
                parent = parent,
                viewHolders = holders
            )
            else -> throw IllegalArgumentException("Unsupported orientation: $orientation")
        }
    }

    private fun drawVertical(
        canvas: Canvas,
        parent: RecyclerView,
        viewHolders: Sequence<View>
    ) {
        val left = parent.paddingLeft.toFloat()
        val right = parent.width.toFloat() - parent.paddingRight

        viewHolders.forEach { holder ->
            val top = holder.bottom.toFloat()
            val bottom = top + decorationFullSize
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
                val dividerCenterY = dividerRect.centerY()

                canvas.drawLine(
                    dividerRect.left + marginStart,
                    dividerCenterY,
                    dividerRect.right - marginEnd,
                    dividerCenterY,
                    dividerPaint
                )
            }
        }
    }

    private fun drawHorizontal(
        canvas: Canvas,
        parent: RecyclerView,
        viewHolders: Sequence<View>
    ) {
        val top = parent.paddingTop.toFloat()
        val bottom = parent.height.toFloat() - parent.paddingBottom

        viewHolders.forEach { holder ->
            val right = holder.right.toFloat()
            val left = right + decorationFullSize
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
                val dividerCenterX = dividerRect.centerX()
                canvas.drawLine(
                    dividerCenterX,
                    dividerRect.top + marginStart,
                    dividerCenterX,
                    dividerRect.bottom - marginEnd,
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
        val itemPos = parent.getChildAdapterPosition(view)
        val itemsCount = parent.adapter?.itemCount?.minus(1)
        if (itemPos != itemsCount || (itemPos == itemsCount && shouldDecorateLastItem)) {
            if (orientation == RecyclerView.VERTICAL)
                outRect.bottom = decorationFullSize.toInt()
            else
                outRect.right = decorationFullSize.toInt()
        }
    }

    class Builder(private val context: Context) {
        internal var orientation: Int = RecyclerView.VERTICAL
        internal var marginStart: Float = 0f
        internal var marginEnd: Float = 0f
        internal var size: Int = 0
        internal val dividerPaint = Paint().apply {
            style = Paint.Style.STROKE
            color = Color.parseColor(COLOR_TRANSPARENT_HEX)
        }
        internal val decorationPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.parseColor(COLOR_TRANSPARENT_HEX)
        }
        internal var shouldDecorateLastItem: Boolean = false

        fun setOrientation(orientation: Int): Builder {
            this.orientation = orientation
            return this
        }

        fun setDividerColorRes(@ColorRes colorRes: Int): Builder {
            dividerPaint.color = ContextCompat.getColor(
                context,
                colorRes
            )
            return this
        }

        fun setDividerColor(@ColorInt color: Int): Builder {
            dividerPaint.color = color
            return this
        }

        fun setMargin(margin: Float): Builder {
            this.marginEnd = margin
            this.marginStart = margin
            return this
        }

        fun setDividerMarginStart(margin: Float): Builder {
            this.marginEnd = margin
            return this
        }

        fun setDividerMarginEnd(margin: Float): Builder {
            this.marginStart = margin
            return this
        }

        fun setShouldDecorateLastItem(isDecorate: Boolean): Builder {
            shouldDecorateLastItem = isDecorate
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