package com.example.kotlinandroidexample.views.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.kotlinandroidexample.databinding.ViewExploreListItemBinding
import com.example.kotlinandroidexample.models.Restaurant

class ExploreListItemView(context: Context, val restaurant: Restaurant) : View(context) {
    private val binding by lazy {
        ViewExploreListItemBinding.inflate(
            LayoutInflater.from(context)
        )
    }

}