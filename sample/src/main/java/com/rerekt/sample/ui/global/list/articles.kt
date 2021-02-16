package com.rerekt.sample.ui.global.list

import androidx.core.content.ContextCompat
import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R
import com.rerekt.sample.databinding.ListItemBinding

data class Article(
    val id: Int,
    val title: String,
    val description: String
)

fun articlesBinder(
    onClick: (Article) -> Unit = {}
) = viewBinder<Article, ListItemBinding>(
    layoutResId = R.layout.list_item,
    binder = { ListItemBinding.bind(it) },
	isForItem = { it is Article },
    areItemsSame = { old, new -> old.id == new.id },
    areContentsSame = { old, new -> old == new },
) {
	bindView { data ->
        println("on item bind")
        llContainer.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                if (position % 2 == 0)
                    android.R.color.holo_red_dark
                else
                    android.R.color.holo_green_dark
            )
        )
        tvTitle.text = data.title
        tvDescription.text = data.description
        tvPosition.text = getString(R.string.position, position)
        setOnClickListener { onClick.invoke(data) }
	}

    onDetachedFromWindow {
        println("on item detached from window")
    }

    onAttachedToWindow {
        println("on item attached to window")
    }

}
