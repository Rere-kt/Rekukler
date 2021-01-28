package com.rerekt.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rerekt.sample.R

open class AppActivity: AppCompatActivity() {

    private val listFragment: ListFragment
        get() = supportFragmentManager.fragments.filterIsInstance<ListFragment>().firstOrNull()
            ?: ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) { goTo(listFragment) }
    }

    private fun goTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

}