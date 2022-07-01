package com.rerekt.sample.ui.global.list

import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R
import com.rerekt.sample.databinding.ItemLoadingBinding

object Loading

fun loadingBinder() = viewBinder<Loading, ItemLoadingBinding>(
    binder = { layoutInflater, parent -> ItemLoadingBinding.inflate(layoutInflater, parent, false) }
)
