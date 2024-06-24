package com.example.kotlinandroidexample

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Matrix.ScaleToFit
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.toColorInt
import com.example.kotlinandroidexample.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        //Random 100 marker
//        for (i in 0..500) {
//            val lat = -90 + (180 * Math.random())
//            val lon = -180 + (360 * Math.random())
//            val latLng = LatLng(lat, lon)
//            val markerOption = MarkerOptions().position(latLng).title("Marker in $lat, $lon")
//            markerOption.icon(setIcon(this, R.drawable.anchor_gg_marker))
//            mMap.addMarker(markerOption)
//        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val markerOption = MarkerOptions().position(sydney).title("Marker in Sydney")
        markerOption.icon(setIcon(this, R.drawable.anchor_gg_marker))

        mMap.addMarker(markerOption)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun setIcon(context: Context, drawableId: Int): BitmapDescriptor {
        val drawable = ActivityCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        val shadow =
            addShadow(
                bitmap,
                drawable.intrinsicHeight,
                drawable.intrinsicWidth,
                0xFF707070.toInt(),
                10,
                0f,
                15f
            );
        return BitmapDescriptorFactory.fromBitmap(shadow)
    }

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
        scaleToFit.setRectToRect(src, dst, ScaleToFit.CENTER)

        val dropShadow = Matrix(scaleToFit)
        dropShadow.postTranslate(dx, dy)

        val maskCanvas = Canvas(mask)
        val paint = Paint()
        maskCanvas.drawBitmap(bm, dropShadow, paint)

        val filter = BlurMaskFilter(size.toFloat(), Blur.NORMAL)
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