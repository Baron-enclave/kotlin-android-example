package com.example.kotlinandroidexample.views

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.ActivityMapsBinding
import com.example.kotlinandroidexample.models.Restaurant
import com.example.kotlinandroidexample.views.map.RestaurantMarker
import com.example.kotlinandroidexample.models.mRestaurants
import com.example.kotlinandroidexample.viewmodels.MapViewModel
import com.example.kotlinandroidexample.views.map.MapMarkersRenderer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

private const val FINE_PERMISSION_CODE = 1


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: Location? = null
    private lateinit var fusedOrientationProviderClient: FusedLocationProviderClient

    private val mMapRestaurantMarker = mutableMapOf<Restaurant, Marker?>()
    private var prevMarker: Marker? = null
    private lateinit var icon: Bitmap
    private lateinit var highlightIcon: Bitmap
    private lateinit var viewModel: MapViewModel

    private lateinit var clusterManager: ClusterManager<RestaurantMarker>
    private lateinit var mapRenderer: MapMarkersRenderer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MapViewModel()

        fusedOrientationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        icon = markerIcon(
//            this,
//            R.drawable.anchor_gg_marker
//        )
//        highlightIcon = markerIcon(
//            this,
//            R.drawable.anchor_gg_marker_highlight
//        )
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getLastLocation()
    }

    private fun setUpClusterManager() {
        if (!this::clusterManager.isInitialized) {
            clusterManager = ClusterManager(this, mMap)
        }

        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)

        mapRenderer = MapMarkersRenderer(this, mMap, clusterManager)
        clusterManager.renderer = mapRenderer


        clusterManager.clearItems()

        clusterManager.addItems(
            mRestaurants.map {
                RestaurantMarker(
                    titleText = it.comment?.text ?: "",
                    location = LatLng(it.latitude, it.longitude),
                    url = it.imageUrl ?: "",
                    icon = RestaurantMarker.Icon.Placeholder(it.imageUrl ?: "")
                )

            }
        )




    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        fusedOrientationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        viewModel.mRestaurantsLiveData.observe(this) {
//            it.forEach { restaurant ->
//                if (!mMapRestaurantMarker.containsKey(restaurant)) {
//                    val latLng = LatLng(restaurant.latitude, restaurant.longitude)
//                    val markerOptions = MarkerOptions().position(latLng).title(restaurant.name)
//                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
//                    markerOptions.visible(false)
//                    mMapRestaurantMarker[restaurant] = mMap.addMarker(markerOptions)
//                }
//            }
//        }

        setUpClusterManager()
//        val markerView = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
//            R.layout.view_map_marker,
//            null
//        )
//        val markerCardView = markerView.findViewById<View>(R.id.markerCardView)
//        val text = markerView.findViewById<TextView>(R.id.content)
//

//        for (res in mRestaurants) {
//
//            text.text = res.comment?.text
//            Glide.with(this).load(res.comment?.avatarUrl)
//                .into(markerView.findViewById(R.id.profile_image))
//            val bitmap = Bitmap.createScaledBitmap(
//                viewToBitmap(markerView),
//                markerCardView.width,
//                markerView.height,
//                true
//            )
//            val markerViewIcon = BitmapDescriptorFactory.fromBitmap(
//                addShadow(
//                    bitmap,
//                    markerCardView.height,
//                    markerCardView.width,
//                    0xFF707070.toInt(),
//                    10,
//                    0f,
//                    15f
//                )
//            )
//
//            val latLng = LatLng(res.latitude, res.longitude)
//            val markerOptions = MarkerOptions().position(latLng)
//            markerOptions.icon(markerViewIcon)
//            mMap.addMarker(markerOptions)
//        }

//        val latLng = LatLng(16.4637, 107.5909)
//        val markerOptions = MarkerOptions().position(latLng)
//        markerOptions.icon(markerViewIcon)
//        mMap.addMarker(markerOptions)

//        mMap.apply {
//            setOnCameraMoveListener {
//
//                val zoomLevel = mMap.cameraPosition.zoom
//                mMapRestaurantMarker.forEach {
//                    val markerOption = it.value
//                    val restaurant = it.key
//                    markerOption?.isVisible = isDisplayMapMarker(zoomLevel, restaurant.rating)
//
//                }
////                viewModel.getRestaurants(mMap.projection.visibleRegion.latLngBounds, zoomLevel)
//
//            }
//            setOnMapClickListener {
//                prevMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
//                prevMarker = null
//
//            }
//            setOnMarkerClickListener {
//                if (it == prevMarker) {
//                    return@setOnMarkerClickListener false
//                }
//                prevMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
//                prevMarker = it
//
//                val highlightIcon =
//                    BitmapDescriptorFactory.fromBitmap(
//                        Bitmap.createScaledBitmap(
//                            highlightIcon,
//                            250,
//                            250,
//                            true
//                        )
//                    )
//                it.setIcon(highlightIcon)
//                false
//            }
//        }

        // Hue city Vietnam
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
//                LatLng(
//                    currentLocation?.latitude ?: 16.4637,
//                    currentLocation?.longitude ?: 107.5909
//                ),
                LatLng(16.4637, 107.5909),
                10f
            )
        )
    }


//    private val minZoomLevelToShow = 6
//    private val maxZoomLevelToShow = 12
//    private val maxRating = 5
//    private val minRating = 0
//    private fun isDisplayMapMarker(zoomLevel: Float, rating: Float): Boolean {
//        // rating 0 -> 5
//        val zoomThresholds =
//            minZoomLevelToShow +
//                    ((rating - minRating) * (maxZoomLevelToShow - minZoomLevelToShow)
//                            / (maxRating - minRating))
//        return zoomLevel >= zoomThresholds
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

}