package com.example.kotlinandroidexample.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinandroidexample.models.Restaurant
import com.google.android.gms.maps.model.LatLngBounds

class MapViewModel : ViewModel() {
    val mRestaurantsLiveData = MutableLiveData<MutableList<Restaurant>>()
    private val mExistsRestaurants = mutableSetOf<Restaurant>()
    fun getRestaurants(latLngBounds: LatLngBounds, zoomLevel: Float) {
        //random restaurants in the bounds
        val existsRestaurants = mExistsRestaurants.filter {
            it.latitude < latLngBounds.northeast.latitude && it.latitude > latLngBounds.southwest.latitude
                    && it.longitude < latLngBounds.northeast.longitude && it.longitude > latLngBounds.southwest.longitude
        }
        if (existsRestaurants.size < 10) {
            for (i in 0..10 - existsRestaurants.size) {
                val restaurant = Restaurant(
                    id = i.toString(),
                    name = "Restaurant $i",
                    latitude = latLngBounds.center.latitude + Math.random() * intArrayOf(
                        -1,
                        1
                    ).random(),
                    longitude = latLngBounds.center.longitude + Math.random() * intArrayOf(
                        -1,
                        1
                    ).random(),
                    rating = Math.random().toFloat() * 5.0f
                )
                mExistsRestaurants.add(restaurant)
            }
        }


        mRestaurantsLiveData.value =
            (mRestaurantsLiveData.value ?: mutableListOf()).also {
                it.addAll(existsRestaurants)
                if (it.size > 50) {
                    it.removeAll(it.subList(0, it.size - 50))
                    Log.d("MapViewModel", "getRestaurants: ${it.size}")
                }
            }


    }
}