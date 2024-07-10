package com.example.kotlinandroidexample.views.map

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.Restaurant
import com.example.kotlinandroidexample.models.getRestaurants
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

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    private var markerCreationSubject: PublishSubject<List<MarkerCreationData>> =
        PublishSubject.create()
    private var zoomEventSubject: PublishSubject<ZoomEventData> =
        PublishSubject.create()
    private lateinit var markerCreationDisposable: Disposable
    private lateinit var zoomEventDisposable: Disposable

    private var restaurantMarkerMap: MutableMap<Restaurant, MarkerInfo?> =
        mutableMapOf()
    private lateinit var dotMarker: BitmapDescriptor
    private lateinit var context: Context
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
        this.context = context

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dotMarker = MarkerUtils.getDotIcon(view.context)
        val res = getRestaurants()
        restaurantMarkerMap.putAll(res.map { it to null })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (!this::mMap.isInitialized) {
            mMap = googleMap
            markerCreationDisposable = markerCreationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listCreationData ->
                    ValueAnimator.ofFloat(1f, 0f).apply {
                        duration = 150
                        interpolator = FastOutSlowInInterpolator()
                        addUpdateListener {
                            for (item in listCreationData) {
                                val oldMarker = restaurantMarkerMap[item.restaurant]?.marker
                                oldMarker?.apply {
                                    alpha = animatedValue as Float
                                }
                            }

                        }
                        doOnEnd { _ ->
                            for (item in listCreationData) {
                                val oldMarker = restaurantMarkerMap[item.restaurant]?.marker
                                oldMarker?.remove()

                                restaurantMarkerMap[item.restaurant] = null
                                if (item.markerType != MarkerType.HIDE) {
                                    val newMarker = mMap.addMarker(item.newMarkerOptions!!.apply {
                                        alpha(0f)
                                    })
                                    restaurantMarkerMap[item.restaurant] =
                                        MarkerInfo(newMarker, item.markerType)

                                }
                            }
                            ValueAnimator.ofFloat(0f, 1f).apply {
                                duration = 200
                                interpolator = LinearOutSlowInInterpolator()
                                addUpdateListener {

                                    for (item in listCreationData) {
                                        val newMarkerInfo = restaurantMarkerMap[item.restaurant]
                                        newMarkerInfo?.marker?.apply {
                                            alpha = it.animatedValue as Float
                                            if (newMarkerInfo.markerType == MarkerType.PILL) {
                                                setAnchor(
                                                    0.5f,
                                                    1.5f - it.animatedValue as Float
                                                )
                                            }

                                        }

                                    }

                                }


                            }.start()

                        }
                    }.start()
                }

            zoomEventDisposable = zoomEventSubject.observeOn(Schedulers.io())
                .subscribe(this::updateMarkers)
            mMap.apply {
                animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(16.4637, 107.5909),
                        13f
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

    private val pillBitmapDescriptorMap = mutableMapOf<Restaurant, BitmapDescriptor>()
    private fun updateMarkers(zoomEventData: ZoomEventData) {
        var batchingListMarkerUpdate: MutableList<MarkerCreationData> = mutableListOf()
        val batchingSize = 100
        for (restaurant in restaurantMarkerMap.keys) {
            val latLng = LatLng(
                restaurant.latitude,
                restaurant.longitude
            )
            if (zoomEventData.latLngBounds.contains(latLng)) {
                val markerType = computeMarkerType(zoomEventData.zoomLevel, restaurant)
                if (restaurantMarkerMap[restaurant]?.markerType != markerType) {
                    val iconBitmapDescriptor = when (markerType) {
                        MarkerType.PILL -> {
                            val cacheBitMap = pillBitmapDescriptorMap[restaurant]
                            if (cacheBitMap == null) {
                                val bitmap = MarkerUtils.getPillIcon(context, restaurant)
                                pillBitmapDescriptorMap[restaurant] = bitmap
                            }
                            pillBitmapDescriptorMap[restaurant]
                        }

                        MarkerType.DOT -> dotMarker
                        MarkerType.HIDE -> null
                    }
                    val markerOptions = MarkerOptions().apply {
                        icon(iconBitmapDescriptor)
                        position(latLng)

                    }
                    batchingListMarkerUpdate.add(
                        MarkerCreationData(
                            markerType,
                            markerOptions,
                            restaurant
                        )
                    )
                }
            } else {
                batchingListMarkerUpdate.add(
                    MarkerCreationData(
                        MarkerType.HIDE,
                        null,
                        restaurant
                    )
                )
            }
            if (batchingListMarkerUpdate.size == batchingSize) {
                Thread.sleep(100)
                markerCreationSubject.onNext(batchingListMarkerUpdate)
                batchingListMarkerUpdate = mutableListOf()
            }
        }
        if (batchingListMarkerUpdate.isNotEmpty()) {
            markerCreationSubject.onNext(batchingListMarkerUpdate)
//            batchingListMarkerUpdate = mutableListOf()
        }
    }


    private fun computeMarkerType(
        zoomLevel: Float,
        restaurant: Restaurant
    ): MarkerType {
        val minZoomLevelToShow = 11
        val maxZoomLevelToShow = 17
        val maxRating = 5f
//        val minRating = 0f

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


    private data class ZoomEventData(val zoomLevel: Float, val latLngBounds: LatLngBounds)
    private data class MarkerInfo(val marker: Marker?, val markerType: MarkerType)

    private data class MarkerCreationData(
        val markerType: MarkerType,
        val newMarkerOptions: MarkerOptions?,
        val restaurant: Restaurant
    )

    enum class MarkerType {
        PILL, DOT, HIDE
    }
}