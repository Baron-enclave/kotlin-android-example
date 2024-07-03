package com.example.kotlinandroidexample.views.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlinandroidexample.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ExploreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet)).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            halfExpandedRatio = 0.6f
            hideFriction = 0.3f
            isHideable = true
        }
        return view
    }

}