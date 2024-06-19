package com.example.kotlinandroidexample.views

import android.content.Intent
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.provider.CalendarContract.Colors
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.databinding.ActivityCharacterDetailBinding
import com.example.kotlinandroidexample.databinding.ActivityMainBinding
import com.example.kotlinandroidexample.models.HPCharacter
import com.example.kotlinandroidexample.viewmodels.CharacterDetailViewModel
import com.example.kotlinandroidexample.views.utils.parcelable

class CharacterDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var viewModel: CharacterDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val character = intent.parcelable<HPCharacter>("character")

        viewModel = CharacterDetailViewModel(character)
        binding.viewModel = viewModel
        //Use Glide to load the image
        Glide.with(this).load(character?.image).into(binding.ivCharacterImage)
    }


}
