package com.example.kotlinandroidexample.views

import android.Manifest
import android.animation.ValueAnimator
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.ActivityMapsBinding
import com.example.kotlinandroidexample.models.Restaurant
import com.example.kotlinandroidexample.models.getRestaurants
import com.example.kotlinandroidexample.views.map.MarkerUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

const val FINE_PERMISSION_CODE = 1


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedOrientationProviderClient: FusedLocationProviderClient

    private var markerCreationSubject: PublishSubject<MarkerCreationData> = PublishSubject.create()
    private var zoomEventSubject: PublishSubject<ZoomEventData> = PublishSubject.create()
    private lateinit var markerCreationDisposable: Disposable
    private lateinit var zoomEventDisposable: Disposable

    private var restaurantMarkerMap: MutableMap<Restaurant, MarkerInfo?> = mutableMapOf()
    private lateinit var dotMarker: BitmapDescriptor
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedOrientationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getLastLocation()
        dotMarker = MarkerUtils.getDotIcon(this)
        val res = getRestaurants()
        restaurantMarkerMap.putAll(res.map { it to null })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        if (!this::mMap.isInitialized) {
            mMap = googleMap
            markerCreationDisposable = markerCreationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->

                    val oldMarker = restaurantMarkerMap[it.restaurant]?.marker
                    ValueAnimator.ofFloat(1f, 0f).apply {
                        duration = 300
                        interpolator = LinearOutSlowInInterpolator()
                        addUpdateListener {
                            oldMarker?.apply {
                                alpha = animatedValue as Float
                                setAnchor(
                                    0f, 1 - animatedValue as Float
                                )
                            }
                        }
                        doOnEnd { _ ->
                            oldMarker?.remove()
                            restaurantMarkerMap[it.restaurant] = null
                            if (it.iconType != MarkerType.HIDE) {
                                val newMarker = mMap.addMarker(it.newMarkerOptions.apply {
                                    alpha(0f)
                                })
                                restaurantMarkerMap[it.restaurant] =
                                    MarkerInfo(newMarker, it.iconType)
                                ValueAnimator.ofFloat(0f, 1f).apply {
                                    duration = 300
                                    interpolator = LinearOutSlowInInterpolator()
                                    addUpdateListener {
                                        newMarker?.apply {
                                            alpha = it.animatedValue as Float
                                            setAnchor(
                                                0f,
                                                1 - it.animatedValue as Float
                                            )
                                        }
                                    }
                                }.start()
                            }
                        }
                    }.start()
                }

            zoomEventDisposable = zoomEventSubject.observeOn(Schedulers.io())
                .subscribe(this::updateMarkers)
            mMap.apply {
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(16.4637, 107.5909),
                        12f
                    )
                )
                setOnCameraIdleListener {
                    zoomEventSubject.onNext(
                        ZoomEventData(
                            mMap.cameraPosition.zoom,
                            mMap.projection.visibleRegion.latLngBounds
                        )
                    )
                }
            }
        }
    }

    private fun updateMarkers(zoomEventData: ZoomEventData) {
        for (restaurant in restaurantMarkerMap.keys) {
            val latLng = LatLng(
                restaurant.latitude,
                restaurant.longitude
            )
            if (zoomEventData.latLngBounds.contains(latLng)) {
                val markerType = computeMarkerType(zoomEventData.zoomLevel, restaurant)
                if (restaurantMarkerMap[restaurant]?.markerType != markerType) {
                    val iconBitmapDescriptor = when (markerType) {
                        MarkerType.PILL -> MarkerUtils.getPillIcon(this, restaurant)
                        MarkerType.DOT -> dotMarker
                        MarkerType.HIDE -> null
                    }
                    val markerOptions = MarkerOptions().apply {
                        icon(iconBitmapDescriptor)
                        position(latLng)

                    }
                    markerCreationSubject.onNext(
                        MarkerCreationData(
                            markerType,
                            markerOptions,
                            restaurant
                        )
                    )
                }
            }

        }
    }


    private fun computeMarkerType(
        zoomLevel: Float,
        restaurant: Restaurant
    ): MarkerType {
        val minZoomLevelToShow = 11
        val maxZoomLevelToShow = 17
        val maxRating = 5f
        val minRating = 0f

        if (restaurant.rating == maxRating) {
            return MarkerType.PILL
        } else {
            val ratio = (maxRating - restaurant.rating) / maxRating
            val zoomThresholds =
                minZoomLevelToShow + ratio * (maxZoomLevelToShow - minZoomLevelToShow)

            return if (zoomLevel < zoomThresholds - 1.5) {
                MarkerType.HIDE
            } else if (zoomLevel < zoomThresholds) {
                MarkerType.DOT
            } else {
                MarkerType.PILL
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        markerCreationDisposable.dispose()
        zoomEventDisposable.dispose()
    }

    private data class ZoomEventData(val zoomLevel: Float, val latLngBounds: LatLngBounds)
    private data class MarkerInfo(val marker: Marker?, val markerType: MarkerType)

    private data class MarkerCreationData(
        val iconType: MarkerType,
        val newMarkerOptions: MarkerOptions,
        val restaurant: Restaurant
    )

    enum class MarkerType {
        PILL, DOT, HIDE
    }

    companion object {
        const val TAG = "MAP_ACTIVITY"
    }

}