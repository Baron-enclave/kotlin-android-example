package com.example.kotlinandroidexample.views.map

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.kotlinandroidexample.databinding.ViewMapMarkerBinding

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

            is RestaurantMarker.Icon.Placeholder ->{
                val drawable = ResourcesCompat.getDrawable(
                    resources,
                    android.R.drawable.ic_menu_mylocation,
                    null
                )
                drawable
            }
        }
        return drawable
    }
}