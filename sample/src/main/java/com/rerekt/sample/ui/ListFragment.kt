package com.rerekt.sample.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.rerekt.rekukler.*
import com.rerekt.sample.R
import com.rerekt.sample.databinding.FragmentMainBinding
import com.rerekt.sample.ui.global.dip
import com.rerekt.sample.ui.global.int
import com.rerekt.sample.ui.global.list.*

class ListFragment: Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding

    private val articlesAdapter by lazy {
        MultiBindingAdapter(
            articlesBinder { println("Click from Article item") },
            loadingBinder()
        )
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initRecycler()
        postDelayedListUpdate()
    }

    @ExperimentalStdlibApi
    private fun postDelayedListUpdate() {
        articlesAdapter.items = buildList {
            addAll(
                (0..20).map {
                    Article(
                        id = it,
                        title = "Title#$it",
                        description = "Description#$it"
                    )
                }
            )
            add(Loading)
        }
    }

    private fun initRecycler() {
        binding.rvArticles.apply {
            configure(articlesAdapter) {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                }
                dividerItemDecoration(
                    size = 2.dip(resources).toInt()
                )
                itemTouchHelper()
            }
        }
    }
}