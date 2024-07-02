package com.example.kotlinandroidexample.views

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.ActivityMapsBinding
import com.example.kotlinandroidexample.models.mRestaurants
import com.example.kotlinandroidexample.viewmodels.MapViewModel
import com.example.kotlinandroidexample.views.map.MapMarkersRenderer
import com.example.kotlinandroidexample.views.map.RestaurantMarker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager

private const val FINE_PERMISSION_CODE = 1


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: Location? = null
    private lateinit var fusedOrientationProviderClient: FusedLocationProviderClient
    private lateinit var clusterManager: ClusterManager<RestaurantMarker>
    private lateinit var mapRenderer: MapMarkersRenderer
    private var currentMarkersSet = mutableSetOf<RestaurantMarker>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        fusedOrientationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getLastLocation()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpClusterManager()
        // Hue city Vietnam
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(16.4637, 107.5909),
                15f
            )
        )
    }

    private fun setUpClusterManager() {
        if (!this::clusterManager.isInitialized) {
            clusterManager = ClusterManager(this, mMap)
        }

        mMap.apply {
            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
        }


        mapRenderer = MapMarkersRenderer(this, mMap, clusterManager) {
            currentMarkersSet.forEach { marker ->
                if (marker.icon.url == it.url) {
                    clusterManager.updateItem(marker)
                    clusterManager.cluster()
                }
            }
        }

        clusterManager.renderer = mapRenderer
        clusterManager.clearItems()
        setMarkers(mRestaurants.map {
            RestaurantMarker(
                titleText = it.comment?.text ?: "",
                location = LatLng(it.latitude, it.longitude),
                icon = RestaurantMarker.Icon.Placeholder(it.comment?.avatarUrl ?: "")
            )
        })
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
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

    private fun setMarkers(markers: List<RestaurantMarker>) {
        val newMarkersSet = markers.toMutableSet()
        val removedElements = currentMarkersSet - newMarkersSet
        val addedElements = newMarkersSet - currentMarkersSet
        clusterManager.apply {
            removedElements.forEach { removeItem(it) }
            addedElements.forEach { addItem(it) }
            currentMarkersSet = newMarkersSet
        }
    }


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