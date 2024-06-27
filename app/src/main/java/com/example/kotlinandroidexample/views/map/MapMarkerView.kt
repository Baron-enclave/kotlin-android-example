package com.example.kotlinandroidexample.views.map

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.ViewMapMarkerBinding

class MapMarkerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAtt: Int = 0,

    ) : ConstraintLayout(context, attrs, defStyleAtt) {
    private val binding by lazy {
        ViewMapMarkerBinding.inflate(
            LayoutInflater.from(context)
        )
    }

    init {
        addView(binding.root)
    }

    fun setContent(
        content: MapMarkerContent,
        title: String?,
    ) {
        when (content) {
            is MapMarkerContent.Cluster -> {
                binding.content.text = content.count.toString()
            }

            is MapMarkerContent.Marker -> {
                binding.content.text = title
//                Glide.with(context)
//                    .load(content.mapMarkerIcon.url)
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .into(binding.profileImage)
            }
        }
//        binding.content.text = title
//        Glide.with(context)
//            .load(imageUrl)
//            .placeholder(R.drawable.ic_launcher_background)
//            .into(binding.profileImage)
    }

    sealed interface MapMarkerContent {

        data class Cluster(
            val count: Int,
        ) : MapMarkerContent

        data class Marker(
            val mapMarkerIcon: RestaurantMarker.Icon,
        ) : MapMarkerContent
    }
}