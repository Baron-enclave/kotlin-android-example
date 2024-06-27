package com.example.kotlinandroidexample.views.map

import android.graphics.Bitmap
import com.example.kotlinandroidexample.models.Restaurant
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class RestaurantMarker(
    val icon: Icon,
    val url: String,
    val titleText: String,
    val location: LatLng
) : ClusterItem {
    override fun getPosition(): LatLng = location

    override fun getTitle(): String = titleText

    override fun getSnippet(): String? = null

    override fun getZIndex(): Float = 0f

    sealed interface Icon {

        val url: String

        data class Placeholder(override val url: String) : Icon
//
        data class BitmapIcon(override val url: String, val image: Bitmap) : Icon
    }
}