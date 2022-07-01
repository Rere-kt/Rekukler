package com.rerekt.sample.ui.global.list

import androidx.core.content.ContextCompat
import com.rerekt.rekukler.viewBinder
import com.rerekt.sample.R
import com.rerekt.sample.databinding.ListItemBinding
import com.rerekt.sample.ui.global.list.payloads.ArticlesPayload

data class Article(
    val id: Int,
    val title: String,
    val description: String
)

fun articlesBinder(
    onClick: (Article) -> Unit = {}
) = viewBinder<Article, ListItemBinding>(
    binder = { layoutInflater, parent -> ListItemBinding.inflate(layoutInflater, parent, false) },
	isForItem = { it is Article },
    areItemsSame = { old, new -> old.id == new.id },
    areContentsSame = { old, new -> old == new },
    getChangePayload = { old, new ->
        val payload = mutableListOf<ArticlesPayload>()

        if (old.title != new.title) {
            payload.add(ArticlesPayload.TITLE)
        }

        if (old.description != new.description) {
            payload.add(ArticlesPayload.DESCRIPTION)
        }

        payload
    }
) {
	bindView { data ->
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

    handlePayload { article, payload ->
        payload
            .forEach {
                when (it as ArticlesPayload) {
                    ArticlesPayload.TITLE ->
                        tvTitle.text = article.title

                    ArticlesPayload.DESCRIPTION ->
                        tvDescription.text = article.description
                }
            }
    }

    onDetachedFromWindow {
        println("on item detached from window")
    }

    onAttachedToWindow {
        println("on item attached to window")
    }

}