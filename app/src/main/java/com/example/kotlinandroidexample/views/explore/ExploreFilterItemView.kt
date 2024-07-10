package com.example.kotlinandroidexample.views.explore

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.kotlinandroidexample.R
import com.example.kotlinandroidexample.databinding.FragmentExploreBinding
import com.example.kotlinandroidexample.databinding.ViewExploreFilterItemBinding

open class ExploreFilterItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : LinearLayout(context, attrs, defStyleAttr) {
    protected val binding: ViewExploreFilterItemBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewExploreFilterItemBinding.inflate(inflater, this, true)
    }

    fun setTitle(title: String) {
        binding.filterTitle.text = title
        binding.filterTitle.visibility = VISIBLE
    }

    fun setIcon(resourceId: Int) {
        binding.filterIcon.setImageResource(resourceId)
        binding.filterIcon.visibility = VISIBLE
    }

    companion object {
        fun settingFilterItem(context: Context): ExploreFilterItemView = SettingFilterItem(context)
        fun sortFilterItem(context: Context): ExploreFilterItemView = SortFilterItem(context)
        fun moreFilterItem(context: Context): MoreFilterItem = MoreFilterItem(context)
    }

    class SettingFilterItem(context: Context) : ExploreFilterItemView(context) {
        init {
            super.binding.filterTrailIcon.setImageResource(R.drawable.tune)
            super.binding.filterTrailIcon.visibility = VISIBLE
        }
    }

    class SortFilterItem(context: Context) : ExploreFilterItemView(context) {
        init {
            setTitle("Sort")
            super.binding.filterTrailIcon.setImageResource(R.drawable.keyboard_arrow_down)
            super.binding.filterTrailIcon.visibility = VISIBLE
        }
    }

    class MoreFilterItem(context: Context) : ExploreFilterItemView(context) {
        init {
            setTitle("More Filter")
            setIcon(R.drawable.more_horiz)
        }
    }
}