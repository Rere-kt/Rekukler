package com.rerekt.sample.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rerekt.rekukler.configure
import com.rerekt.rekukler.getItems
import com.rerekt.rekukler.updateList
import com.rerekt.sample.R
import com.rerekt.sample.databinding.FragmentMainBinding
import com.rerekt.sample.ui.global.dip
import com.rerekt.sample.ui.global.list.Article
import com.rerekt.sample.ui.global.list.Loading
import com.rerekt.sample.ui.global.list.articlesBinder
import com.rerekt.sample.ui.global.list.loadingBinder

class ListFragment: Fragment(R.layout.fragment_main) {

    lateinit var binding: FragmentMainBinding

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initRecycler()
        postDelayedListUpdate()
    }

    // Just like network async request
    @ExperimentalStdlibApi
    private fun postDelayedListUpdate() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.rvArticles.updateList(
                buildList {
                    addAll(
                        (0..10).map {
                            Article(
                                id = it,
                                title = "Title#$it",
                                description = "Description#$it"
                            )
                        }
                    )
                    add(Loading)
                }
            )
		}, 500)
    }

    private fun initRecycler() {
        binding.rvArticles.configure {
            linearLayout {
                reverseLayout = false
                orientation = LinearLayoutManager.VERTICAL
            }
            viewBinders(
                articlesBinder { println("Click from Article item") },
                loadingBinder()
            )
            dividerItemDecoration(
                size = 2.dip(resources).toInt()
            )
        }
    }

}