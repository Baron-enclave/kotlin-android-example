package com.example.kotlinandroidexample.views.map

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.kotlinandroidexample.databinding.ViewDotMarkerBinding
import com.example.kotlinandroidexample.databinding.ViewMapMarkerBinding
import com.example.kotlinandroidexample.databinding.ViewPillMarkerBinding

class DotMarker(context: Context) : RelativeLayout(context) {
    private val binding by lazy {
        ViewDotMarkerBinding.inflate(LayoutInflater.from(context))
    }

    init {
        addView(binding.root)
    }
}

class PillMarker(context: Context) : LinearLayout(context) {
    private val binding by lazy {
        ViewPillMarkerBinding.inflate(LayoutInflater.from(context))
    }

    init {
        addView(binding.root)
    }
}

class MapMarkerView(context: Context) : ConstraintLayout(context) {
    private val binding by lazy {
        ViewMapMarkerBinding.inflate(
            LayoutInflater.from(context)
        )
    }

    init {
        addView(binding.root)
    }

    fun setContent(
        mapMarkerIcon: RestaurantMarker.Icon,
        title: String?,
    ) {
        val drawable = getIconDrawable(mapMarkerIcon)
        binding.profileImage.setImageDrawable(drawable)
        binding.content.text = title

    }

    private fun getIconDrawable(
        markerIcon: RestaurantMarker.Icon,
    ): Drawable? {

        val drawable = when (markerIcon) {
            is RestaurantMarker.Icon.BitmapIcon -> {
                RoundedBitmapDrawableFactory.create(resources, markerIcon.image)
            }

            is RestaurantMarker.Icon.Placeholder -> {
                ResourcesCompat.getDrawable(
                    resources,
                    android.R.drawable.ic_menu_mylocation, // default icon
                    null
                )
            }
        }
        return drawable
    }
}