package com.example.kotlinandroidexample.views.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.collection.LruCache
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MapMarkersRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<RestaurantMarker>,
    private val onImageLoaded: (icon: RestaurantMarker.Icon) -> Unit
) : DefaultClusterRenderer<RestaurantMarker>(context, map, clusterManager) {

    private val mapMarkerView: MapMarkerView = MapMarkerView(context)
    private val loadedImages = LruCache<String, Bitmap>(10)
    override fun onBeforeClusterItemRendered(
        clusterItem: RestaurantMarker,
        markerOptions: MarkerOptions
    ) {
        val bitmapDescriptor = getItemIcon(clusterItem)
        markerOptions
            .icon(bitmapDescriptor)
    }

    override fun onClusterItemUpdated(clusterItem: RestaurantMarker, marker: Marker) {
        val bitmapDescriptor = getItemIcon(clusterItem)
        marker.setIcon(bitmapDescriptor)
    }

    private fun getItemIcon(marker: RestaurantMarker): BitmapDescriptor {
        val iconToShow: RestaurantMarker.Icon = when (marker.icon) {
            is RestaurantMarker.Icon.Placeholder -> {
                val cachedIcon = loadedImages.get(marker.icon.url)
                if (cachedIcon == null) {
                    loadBitmapImage(marker.icon.url)
                }
                cachedIcon?.let {
                    null
                    RestaurantMarker.Icon.BitmapIcon(marker.icon.url, it)
                } ?: marker.icon
            }

            is RestaurantMarker.Icon.BitmapIcon -> marker.icon

        }
        mapMarkerView.setContent(
            title = marker.titleText,
            mapMarkerIcon = iconToShow
        )

        val bitmap = Bitmap.createScaledBitmap(
            MarkerUtils.viewToBitmap(mapMarkerView),
            mapMarkerView.width,
            mapMarkerView.height,
            true
        )

        val markerViewIcon = BitmapDescriptorFactory.fromBitmap(
            MarkerUtils.addShadow(
                bitmap,
                mapMarkerView.height,
                mapMarkerView.width,
                0xFF707070.toInt(),
                10,
                0f,
                15f
            )
        )
        return markerViewIcon
    }

    private fun loadBitmapImage(url: String) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    val old = loadedImages.put(url, resource)
                    if (old == null) {
                        onImageLoaded.invoke(RestaurantMarker.Icon.BitmapIcon(url, resource))
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    }
}