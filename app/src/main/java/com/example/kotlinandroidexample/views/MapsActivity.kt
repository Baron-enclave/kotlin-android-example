package com.example.kotlinandroidexample.views

import android.Manifest
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
    private var currentLocation: Location? = null
    private lateinit var fusedOrientationProviderClient: FusedLocationProviderClient

    private var restaurantMarkerMap: MutableMap<Restaurant, MarkerInfo?> = mutableMapOf()
    private lateinit var dotMarker: BitmapDescriptor

    private var markerCreationSubject: PublishSubject<IconData> = PublishSubject.create()
    private var zoomEventSubject: PublishSubject<ZoomEventData> = PublishSubject.create()
    private lateinit var markerCreationDisposable: Disposable
    private lateinit var zoomEventDisposable: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedOrientationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getLastLocation()
        dotMarker = getDotIcon()
        val res = getRestaurants()
        restaurantMarkerMap.putAll(res.map { it to null })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        if (!this::mMap.isInitialized) {
            mMap = googleMap
            markerCreationDisposable = markerCreationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val oldMarker = restaurantMarkerMap[it.restaurant]?.marker
                    oldMarker?.remove()
                    restaurantMarkerMap[it.restaurant] = null
                    if (it.iconType != MarkerType.HIDE) {
                        val newMarker = mMap.addMarker(it.newMarkerOptions)
                        restaurantMarkerMap[it.restaurant] = MarkerInfo(newMarker, it.iconType)
                    }
                }

            zoomEventDisposable = zoomEventSubject.observeOn(Schedulers.io())
                .subscribe {
                    updateMarkers(it)
                }
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
        Log.d(TAG, "updateMarkers: current thread: ${Thread.currentThread().name}")
        for (restaurant in restaurantMarkerMap.keys) {
            if (!zoomEventData.latLngBounds.contains(
                    LatLng(
                        restaurant.latitude,
                        restaurant.longitude
                    )
                )
            ) {
                continue
            }
            val iconType = computeIconType(zoomEventData.zoomLevel, restaurant)
            if (restaurantMarkerMap[restaurant]?.iconType != iconType) {
                val iconBitmapDescriptor = when (iconType) {
                    MarkerType.PILL -> getPillIcon(restaurant)
                    MarkerType.DOT -> dotMarker
                    MarkerType.HIDE -> null
                }
                val markerOptions = MarkerOptions().apply {
                    icon(iconBitmapDescriptor)
                    position(LatLng(restaurant.latitude, restaurant.longitude))
                }
                markerCreationSubject.onNext(
                    IconData(
                        iconType,
                        markerOptions,
                        restaurant
                    )
                )
            }
        }
    }


    private fun computeIconType(
        zoomLevel: Float,
        restaurant: Restaurant
    ): MarkerType {
        val minZoomLevelToShow = 11
        val maxZoomLevelToShow = 17
        val maxRating = 5
        val minRating = 0

        if (restaurant.rating == 5.0f) return MarkerType.PILL
        else {
            val zoomThresholds =
                minZoomLevelToShow +
                        ((maxRating - restaurant.rating) * (maxZoomLevelToShow - minZoomLevelToShow)
                                / (maxRating - minRating))
            return if (zoomLevel < zoomThresholds - 1.5) {
                MarkerType.HIDE
            } else if (zoomLevel < zoomThresholds) {
                MarkerType.DOT
            } else {
                MarkerType.PILL
            }
        }
    }

    private fun getDotIcon(): BitmapDescriptor {
        val dotMarker = View.inflate(this, R.layout.view_dot_marker, null)
        val bitmap = Bitmap.createScaledBitmap(
            MarkerUtils.viewToBitmap(dotMarker),
            dotMarker.width,
            dotMarker.height,
            true
        )

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getPillIcon(restaurant: Restaurant): BitmapDescriptor {
        val pillMarker = View.inflate(this, R.layout.view_pill_marker, null)
        val textView: TextView = pillMarker.findViewById(R.id.pillMarkerRatingText)
        textView.text = restaurant.rating.toString()
        val bitmap = Bitmap.createScaledBitmap(
            MarkerUtils.viewToBitmap(pillMarker),
            pillMarker.width,
            pillMarker.height,
            true
        )

        val markerViewIcon = BitmapDescriptorFactory.fromBitmap(
            MarkerUtils.addShadow(
                bitmap,
                pillMarker.height,
                pillMarker.width,
                0xFF707070.toInt(),
                5,
                0f,
                5f
            )
        )
        return markerViewIcon
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
    private data class MarkerInfo(val marker: Marker?, val iconType: MarkerType)

    private data class IconData(
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