package com.example.kotlinandroidexample.views.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class MapMarkersRenderer(
    val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<RestaurantMarker>,
) : DefaultClusterRenderer<RestaurantMarker>(context, map, clusterManager) {

    private val mapMarkerView: MapMarkerView = MapMarkerView(context)
//
//    private val markerIconGenerator = IconGenerator(context)

    init {
//        markerIconGenerator.setBackground(null)
//        markerIconGenerator.setContentView(mapMarkerView)
    }

    override fun onBeforeClusterItemRendered(
        clusterItem: RestaurantMarker,
        markerOptions: MarkerOptions
    ) {
        val data = getItemIcon(clusterItem)
        markerOptions
            .icon(data.bitmapDescriptor)
//            .anchor(data.anchorU, data.anchorV)
    }

    override fun onClusterItemUpdated(clusterItem: RestaurantMarker, marker: Marker) {
        val data = getItemIcon(clusterItem)
        marker.setIcon(data.bitmapDescriptor)

//        marker.setAnchor(data.anchorU, data.anchorV)
    }

//    override fun onBeforeClusterRendered(
//        cluster: Cluster<RestaurantMarker>,
//        markerOptions: MarkerOptions
//    ) {
////        val icon: Bitmap = markerIconGenerator.makeIcon()
//
//        val data = getClusterIcon(cluster)
//        markerOptions
//            .icon(data.bitmapDescriptor)
////            .anchor(data.anchorU!!, data.anchorV!!)
//    }
//
//    override fun onClusterUpdated(cluster: Cluster<RestaurantMarker>, marker: Marker) {
//        val data = getClusterIcon(cluster)
//
//        marker.setIcon(data.bitmapDescriptor)
////        marker.setAnchor(data.anchorU!!, data.anchorV!!)
//    }

//    override fun shouldRenderAsCluster(cluster: Cluster<RestaurantMarker>): Boolean = cluster.size > 2

    private fun getItemIcon(marker: RestaurantMarker): IconData {

        val iconToShow: RestaurantMarker.Icon = when(marker.icon){
            is RestaurantMarker.Icon.Placeholder -> {
                loadBitmapImage(marker.icon.url)

                RestaurantMarker.Icon.BitmapIcon(marker.icon.url, viewToBitmap(mapMarkerView))
            }
            is RestaurantMarker.Icon.BitmapIcon -> {
                marker.icon
            }

        }
        mapMarkerView.setContent(
            title = marker.titleText,
            content = MapMarkerView.MapMarkerContent.Marker(
                mapMarkerIcon = iconToShow
            )
        )

        val bitmap = Bitmap.createScaledBitmap(
            viewToBitmap(mapMarkerView),
            mapMarkerView.width,
            mapMarkerView.height,
            true
        )

        val markerViewIcon = BitmapDescriptorFactory.fromBitmap(
            addShadow(
                bitmap,
                mapMarkerView.height,
                mapMarkerView.width,
                0xFF707070.toInt(),
                10,
                0f,
                15f
            )
        )

        return IconData(
            bitmapDescriptor = markerViewIcon,
//            anchorU = middleBalloon / 2 / icon.width,
//            anchorV = 1f
        )


    }

    private fun loadBitmapImage(url: String) {
        // Load image from url
                        Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mapMarkerView.findViewById(R.id.profile_image))
    }

//    private fun getClusterIcon(cluster: Cluster<RestaurantMarker>): IconData {
//        mapMarkerView.setContent(
//            MapMarkerView.MapMarkerContent.Cluster(
//                count = cluster.size
//            ),
//            title = null
//        )
//
//        val icon: Bitmap = markerIconGenerator.makeIcon()
////        val middleBalloon = dpToPx(context, 40)
//        return IconData(
//            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(icon),
////            anchorU = middleBalloon / 2 / icon.width,
////            anchorV = 1f
//        )
//    }

    //
//
    private data class IconData(
        val bitmapDescriptor: BitmapDescriptor,
        val anchorU: Float? = null,
        val anchorV: Float? = null,
    )


    private fun viewToBitmap(view: View): Bitmap {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }


//    private fun markerIcon(context: Context, drawableId: Int): Bitmap {
//        val drawable = ActivityCompat.getDrawable(context, drawableId)
//        val bitmap = Bitmap.createBitmap(
//            drawable!!.intrinsicWidth,
//            drawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//        val shadow =
//            addShadow(
//                bitmap,
//                drawable.intrinsicHeight,
//                drawable.intrinsicWidth,
//                0xFF707070.toInt(),
//                10,
//                0f,
//                15f
//            )
//        return shadow
//    }

    private fun addShadow(
        bm: Bitmap,
        dstHeight: Int,
        dstWidth: Int,
        color: Int,
        size: Int,
        dx: Float,
        dy: Float
    ): Bitmap {
        val mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8)

        val scaleToFit = Matrix()
        val src = RectF(0f, 0f, bm.width.toFloat(), bm.height.toFloat())
        val dst = RectF(0f, 0f, dstWidth - 2 * dx, dstHeight - 2 * dy)
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)

        val dropShadow = Matrix(scaleToFit)
        dropShadow.postTranslate(dx, dy)

        val maskCanvas = Canvas(mask)
        val paint = Paint()
        maskCanvas.drawBitmap(bm, dropShadow, paint)

        val filter = BlurMaskFilter(size.toFloat(), BlurMaskFilter.Blur.NORMAL)
        paint.reset()
        paint.color = color
        paint.setMaskFilter(filter)
        paint.isFilterBitmap = true

        val ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888)
        val retCanvas = Canvas(ret)
        retCanvas.drawBitmap(mask, 0f, 0f, paint)
        retCanvas.drawBitmap(bm, scaleToFit, null)
        mask.recycle()
        return ret
    }
}