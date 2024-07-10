package com.example.kotlinandroidexample.views.explore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.Restaurant

class ImageSliderAdapter(val context: Context, private val mRestaurants: List<Restaurant>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderAdapterViewHolder>() {

    class ImageSliderAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.slider_item_bg)!!

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSliderAdapterViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.view_explore_slider_item, parent, false)
        return ImageSliderAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = mRestaurants.size

    override fun onBindViewHolder(holder: ImageSliderAdapterViewHolder, position: Int) {
        val item = mRestaurants[position]
        Glide.with(context).load(item.imageUrl).into(holder.imageView)

    }
}