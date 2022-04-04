package com.rerekt.sample.ui.global.list.payloads

import com.rerekt.rekukler.BasePayload
import com.rerekt.sample.ui.global.list.Article

enum class ArticlesPayload: BasePayload<Article> {
    TITLE,
    DESCRIPTION
}