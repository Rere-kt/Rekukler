package com.rerekt.sample.ui.global.list

import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R
import com.rerekt.sample.databinding.ItemLoadingBinding

object Loading

fun loadingBinder() = viewBinder<Loading, ItemLoadingBinding>(
    R.layout.item_loading,
    binder = { ItemLoadingBinding.bind(it) }
)
