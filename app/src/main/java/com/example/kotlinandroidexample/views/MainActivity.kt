package com.example.kotlinandroidexample.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.ActivityMainBinding
import com.example.kotlinandroidexample.views.community.CommunityFragment
import com.example.kotlinandroidexample.views.explore.ExploreFragment
import com.example.kotlinandroidexample.views.me.MeFragment
import com.example.kotlinandroidexample.views.saved.SavedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mNavBarView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mNavBarView = binding.bottomNavigation
        loadFragment(exploreFragment)

        mNavBarView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.exploreFragment -> {
                    loadFragment(exploreFragment)
                    true
                }

                R.id.communityFragment -> {
                    loadFragment(CommunityFragment())
                    true
                }

                R.id.savedFragment -> {
                    loadFragment(SavedFragment())
                    true
                }

                R.id.meFragment -> {
                    loadFragment(MeFragment())
                    true
                }

                else ->
                    false
            }
        }
    }

    private val exploreFragment = ExploreFragment()
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

