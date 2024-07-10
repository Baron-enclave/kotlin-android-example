package com.example.kotlinandroidexample.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.ActivityMainBinding
import com.example.kotlinandroidexample.views.adapters.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mNavBarView: BottomNavigationView
    private lateinit var mViewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mNavBarView = binding.bottomNavigation
        mViewPager = binding.viewPager

        mViewPager.isUserInputEnabled = false
        setupViewPager()

        mNavBarView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.exploreFragment -> mViewPager.currentItem = 0
                R.id.communityFragment -> mViewPager.currentItem = 1
                R.id.savedFragment -> mViewPager.currentItem = 2
                R.id.meFragment -> mViewPager.currentItem = 3
            }
            true
        }
    }

    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        mViewPager.adapter = viewPagerAdapter
    }
}

