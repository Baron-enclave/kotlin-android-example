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

class ExploreListItemAdapter(val context: Context, val mRestaurants: List<Restaurant>) :
    RecyclerView.Adapter<ExploreListItemAdapter.ExploreListItemViewHolder>() {

    class ExploreListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.ivRestaurantLogo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreListItemViewHolder =
        ExploreListItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.view_explore_list_item, parent, false)
        )

    override fun getItemCount(): Int = mRestaurants.size

    override fun onBindViewHolder(holder: ExploreListItemViewHolder, position: Int) {
        val item = mRestaurants[position]
        Glide.with(context).load(item.imageUrl).into(holder.logo)
    }
}