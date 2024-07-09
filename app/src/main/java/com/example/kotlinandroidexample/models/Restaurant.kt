package com.example.kotlinandroidexample.models

import android.annotation.SuppressLint
import java.util.Locale
import kotlin.random.Random

class Restaurant(
    val id: String,
    val name: String,
    val address: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val rating: Float,
    val comment: Comment? = null,
)

fun getRestaurants(): List<Restaurant> {
    val mRes = mutableListOf<Restaurant>()
    for (i in 1..1000) {
        mRes.add(
            Restaurant(
                "$i",
                "THUAN CHAY - VEGAN",
                null,
                16.4637 + Random.nextDouble(-0.18, 0.18),
                107.59095 + Random.nextDouble(-0.18, 0.18),
//                16.4637 + Random.nextDouble(-0.01, 0.01),
//                107.59095 + Random.nextDouble(-0.01, 0.01),
                "https://images.happycow.net/venues/200x200/28/81/hcmp28812_379199.jpeg",
                String.format(Locale.ENGLISH, "%.1f", (5 * Random.nextFloat())).toFloat(),
                comment = Comment(
                    "https://images.happycow.net/venues/200x200/28/81/hcmp28812_379199.jpeg",
                    "A very nice place to eat vegan food. I love it!",
                )
            ),
        )
    }
    return mRes
}
