package com.example.kotlinandroidexample.views.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.models.HPCharacter

class HPCharactersAdapter(
    private val characters: List<HPCharacter>,
    private val onClick: (HPCharacter) -> Unit
) :
    RecyclerView.Adapter<HPCharactersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = characters[position]

        Glide.with(holder.itemView)
            .load(item.image)
            .into(holder.imageView)
        holder.itemView.setOnClickListener {
            onClick(item)
        }
        holder.textView.text = item.name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = this.itemView.findViewById(R.id.ivItem)
        val textView: TextView = this.itemView.findViewById(R.id.tvItemTitle)
    }
}