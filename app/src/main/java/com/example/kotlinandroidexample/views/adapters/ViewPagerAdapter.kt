package com.example.kotlinandroidexample.views.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kotlinandroidexample.views.community.CommunityFragment
import com.example.kotlinandroidexample.views.explore.ExploreFragment
import com.example.kotlinandroidexample.views.me.MeFragment
import com.example.kotlinandroidexample.views.saved.SavedFragment


class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    companion object {
        const val NUM_PAGES = 4
        const val EXPLORE = 0
        const val COMMUNITY = 1
        const val SAVED = 2
        const val ME = 3
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when (position) {
        EXPLORE -> ExploreFragment()
        COMMUNITY -> CommunityFragment()
        ME -> MeFragment()
        SAVED -> SavedFragment()
        else -> throw IllegalArgumentException("Invalid position")
    }

}