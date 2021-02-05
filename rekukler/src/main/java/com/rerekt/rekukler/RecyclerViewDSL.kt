package com.rerekt.rekukler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.*

fun RecyclerView.configure(
    adapter: MultiBindingAdapter,
    block: RecyclerViewConfig.() -> Unit = {}
) {
    RecyclerViewConfig(context).also {
        block(it)
        layoutManager = it.layoutManager
        this.adapter = adapter
        it.itemDecorations.forEach { addItemDecoration(it) }
        it.itemTouchHelper?.attachToRecyclerView(this)
    }
}

fun RecyclerView.updateList(newList: List<Any>) {
    (adapter as? MultiBindingAdapter)?.apply {
        items = newList
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.getItems() = (adapter as? MultiBindingAdapter)?.items as? List<T>

class RecyclerViewConfig(
    private val context: Context
) {

	internal var itemDecorations: MutableList<RecyclerView.ItemDecoration> = mutableListOf()

    internal var layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    internal var itemTouchHelper: ItemTouchHelper? = null

    fun linearLayout(
        block: LinearLayoutManager.() -> Unit = {}
    ) { layoutManager = LinearLayoutManager(context).apply(block) }

    fun gridLayout(
        spansCount: Int = 1,
        block: GridLayoutManager.() -> Unit = {}
    ) { layoutManager = GridLayoutManager(context, spansCount).apply(block) }

    fun dividerItemDecoration(
        orientation: Int = DividerItemDecoration.VERTICAL,
		block: DividerItemDecoration.() -> Unit
    ) {
        itemDecoration(
            DividerItemDecoration(context, orientation).apply(block)
        )
    }

    fun staggeredGridLayout(
        spansCount: Int = 1,
        orientation: Int = StaggeredGridLayoutManager.VERTICAL,
        block: StaggeredGridLayoutManager.() -> Unit = {}
    ) {
        layoutManager = StaggeredGridLayoutManager(spansCount, orientation).apply(block)
    }

    fun itemTouchHelper(
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
                    viewHolder.itemView.outlineProvider = null
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

	fun dividerItemDecoration(
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

    fun itemDecoration(decoration: RecyclerView.ItemDecoration) {
        itemDecorations.add(decoration)
    }

}