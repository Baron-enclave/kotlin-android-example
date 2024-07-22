package com.example.kotlinandroidexample.views.explore

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.Restaurant
import com.example.kotlinandroidexample.models.getRestaurants
import com.example.kotlinandroidexample.views.explore.adapters.ExploreListItemAdapter
import com.example.kotlinandroidexample.views.explore.adapters.ImageSliderAdapter
import com.example.kotlinandroidexample.views.map.MarkerUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ExploreFragment : Fragment(), OnMapReadyCallback {
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
    lateinit var carouselRcv: RecyclerView
    private lateinit var listFilterItem: LinearLayout
    private lateinit var bottomSheetView: View
    private var prevMarker: Marker? = null

    companion object {
        const val TAG = "ExploreFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.explore_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        carouselRcv = view.findViewById(R.id.explore_crs_vp)
        bottomSheetView = view.findViewById(R.id.bottom_sheet)
        listFilterItem = view.findViewById(R.id.list_filter_item)
        BottomSheetBehavior.from(bottomSheetView).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = 400
        }.apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (carouselRcv.visibility == View.VISIBLE) {
                        carouselRcv.visibility = View.INVISIBLE
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })
        }

        val res = getRestaurants()
        restaurantMarkerMap.putAll(res.map { it to null })
//        view.findViewById<RecyclerView>(R.id.explore_bottomsheet_rcv).apply {
//
//            layoutManager = LinearLayoutManager(view.context)
//            adapter = ExploreListItemAdapter(view.context, getRestaurants())
//        }

//        listFilterItem.apply {
//            addView(ExploreFilterItemView.settingFilterItem(view.context))
//            addView(ExploreFilterItemView.sortFilterItem(view.context))
//            addView(ExploreFilterItemView(view.context).apply {
//                setTitle("Vegan")
//                setIcon(R.drawable.category_vegan)
//            })
//            addView(ExploreFilterItemView(view.context).apply {
//                setTitle("Vegetarian")
//                setIcon(R.drawable.category_vegetarian)
//            })
//            addView(ExploreFilterItemView(view.context).apply {
//                setTitle("Vegan Options")
//                setIcon(R.drawable.category_veg_friendly)
//            })
//            addView(ExploreFilterItemView(view.context).apply {
//                setTitle("Open Now")
//            })
//            addView(ExploreFilterItemView.moreFilterItem(view.context))
//
//        }

//        carouselRcv.apply {
//            adapter = ImageSliderAdapter(view.context, getRestaurants())
//            layoutManager =
//                LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
//        }


        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context

    }

    override fun onDestroy() {
        super.onDestroy()
        markerCreationDisposable.dispose()
        zoomEventDisposable.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dotMarker = MarkerUtils.getDotIcon(context)
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        if (!this::mMap.isInitialized) {
            mMap = googleMap
            markerCreationDisposable = markerCreationSubject
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::createMarkers)

            zoomEventDisposable =
                zoomEventSubject
                    .observeOn(Schedulers.io())
                    .subscribe(this::handleZoomEvent)
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
                setOnMapClickListener {
                    hideCarousel()

                    if (prevMarker != null) {
                        val prevRes = prevMarker!!.tag as Restaurant?
                        if (prevRes != null) {

                            val resetIcon = pillBitmapDescriptorMap[prevRes]
                            prevMarker?.setIcon(resetIcon)
                        }
                        prevMarker = null
                    }
                }

                setOnMarkerClickListener {
                    carouselRcv.visibility = View.VISIBLE

                    if (prevMarker != null) {
                        val prevRes = prevMarker!!.tag as Restaurant?
                        if (prevRes != null) {
                            val resetIcon = pillBitmapDescriptorMap[prevRes]
                            prevMarker?.setIcon(resetIcon)
                        }
                    }
                    val res = it.tag as Restaurant
                    val newIcon: BitmapDescriptor =
                        MarkerUtils.getScalePillIcon(requireView().context, res)
                    it.setIcon(newIcon)
                    prevMarker = it
                    false
                }
            }
        }
    }

    private fun hideCarousel() {
        if (carouselRcv.visibility == View.VISIBLE) {
            carouselRcv.visibility = View.GONE
        }
    }

    private fun createMarkers(listCreationData: List<MarkerCreationData>) {
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
                            MarkerInfo(
                                newMarker?.apply { tag = item.restaurant },
                                item.markerType
                            )

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

    private val pillBitmapDescriptorMap = mutableMapOf<Restaurant, BitmapDescriptor>()
    private fun handleZoomEvent(zoomEventData: ZoomEventData) {
        var batchingListMarkerUpdate: MutableList<MarkerCreationData> = mutableListOf()
        val batchingSize = 100
        let {
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