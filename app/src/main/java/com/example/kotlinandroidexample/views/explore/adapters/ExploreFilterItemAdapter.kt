package com.example.kotlinandroidexample.views.explore.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.Category
import com.example.kotlinandroidexample.models.Restaurant

class ExploreFilterItemAdapter(val context: Context, private val mCategories: List<Category>) :
    RecyclerView.Adapter<ExploreFilterItemAdapter.ExploreFilterItemAdapterViewHolder>() {

    class ExploreFilterItemAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.filter_title)!!
        val iconView = itemView.findViewById<ImageView>(R.id.filter_icon)!!
        val trailIconView = itemView.findViewById<ImageView>(R.id.filter_trail_icon)!!

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreFilterItemAdapterViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.view_explore_filter_item, parent, false)
        return ExploreFilterItemAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = mCategories.size + 3

    override fun onBindViewHolder(holder: ExploreFilterItemAdapterViewHolder, position: Int) {
        when (position) {
            0 -> {

                holder.textView.visibility = View.GONE
                holder.iconView.setImageResource(R.drawable.tune)
                holder.iconView.setPadding(0)
            }

            1 -> {
                holder.textView.text = "Sort"
                holder.iconView.setImageResource(R.drawable.tune)
                holder.textView.setPadding(4, 0, 0, 0)
            }

            mCategories.size -> {
                holder.textView.visibility = View.GONE
                holder.iconView.setImageResource(R.drawable.tune)
                holder.iconView.setPadding(0)
            }

            else -> {
                val item = mCategories[position - 3]
                holder.textView.text = item.title
            }
        }
    }
}