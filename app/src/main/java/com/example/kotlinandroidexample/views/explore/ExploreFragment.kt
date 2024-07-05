package com.example.kotlinandroidexample.views.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.mRestaurants
import com.example.kotlinandroidexample.views.adapters.ExploreListItemAdapter
import com.example.kotlinandroidexample.views.explore.carousel.ImageSliderAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ExploreFragment : Fragment() {
    lateinit var carouselRcv: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        carouselRcv = view.findViewById(R.id.explore_crs_vp)

        BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet)).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = 400
        }.apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (carouselRcv.visibility == View.VISIBLE) {
                        carouselRcv.visibility = View.INVISIBLE
                    }

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })
        }

        view.findViewById<RecyclerView>(R.id.explore_bottomsheet_rcv).apply {

            layoutManager = LinearLayoutManager(view.context)
            adapter = ExploreListItemAdapter(view.context, mRestaurants.toList())
        }

        carouselRcv.apply {
            adapter = ImageSliderAdapter(view.context, mRestaurants.toList())
            layoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        }
        return view
    }

}