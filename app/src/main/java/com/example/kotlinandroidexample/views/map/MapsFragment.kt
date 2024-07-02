package com.example.kotlinandroidexample.views.map

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.mRestaurants
import com.example.kotlinandroidexample.views.FINE_PERMISSION_CODE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var currentLocation: Location? = null
    private lateinit var fusedOrientationProviderClient: FusedLocationProviderClient
    private lateinit var clusterManager: ClusterManager<RestaurantMarker>
    private var currentMarkersSet = mutableSetOf<RestaurantMarker>()
    private lateinit var mapRenderer: MapMarkersRenderer
    private lateinit var activity: AppCompatActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        val mapFragment = getChildFragmentManager()
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
//        getLastLocation()
        return view


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as AppCompatActivity
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
            clusterManager = ClusterManager(context, mMap)
        }

        mMap.apply {
            setOnCameraIdleListener(clusterManager)
            setOnMarkerClickListener(clusterManager)
        }


        mapRenderer = MapMarkersRenderer(activity, mMap, clusterManager) {
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


    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        fusedOrientationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val mapFragment =
                    activity.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        activity.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }


}