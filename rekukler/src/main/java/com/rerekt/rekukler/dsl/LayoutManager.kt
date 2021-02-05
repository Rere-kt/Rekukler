package com.rerekt.rekukler.dsl

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rerekt.rekukler.RecyclerViewConfig

fun RecyclerViewConfig.linearLayout(
    block: LinearLayoutManager.() -> Unit = {}
) { layoutManager = LinearLayoutManager(context).apply(block) }

fun RecyclerViewConfig.gridLayout(
    spansCount: Int = 1,
    block: GridLayoutManager.() -> Unit = {}
) { layoutManager = GridLayoutManager(context, spansCount).apply(block) }

fun RecyclerViewConfig.staggeredGridLayout(
    spansCount: Int = 1,
    orientation: Int = StaggeredGridLayoutManager.VERTICAL,
    block: StaggeredGridLayoutManager.() -> Unit = {}
) {
    layoutManager = StaggeredGridLayoutManager(spansCount, orientation).apply(block)
}