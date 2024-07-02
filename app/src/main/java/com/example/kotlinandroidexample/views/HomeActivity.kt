package com.example.kotlinandroidexample.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kotlinandroidexample.DBHelper
import com.example.kotlinandroidexample.databinding.ActivityHomeBinding
import com.example.kotlinandroidexample.services.AuthService
import com.example.kotlinandroidexample.services.FakeAuthService

class HomeActivity : AppCompatActivity() {
    // Creating FakeAuthService in both HomeActivity and MainActivity isn't necessary
    // Need a DI container to create it once
    private var dbHelper = DBHelper(this, null)
    private val authService: AuthService = FakeAuthService()

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        Glide.with(this)
//            .load("https://images.happycow.net/venues/200x200/35/23/hcmp352305_2150589.jpeg")
//            .into(binding.ivLogo)
//        val bitmap = Bitmap.createScaledBitmap(
//            viewToBitmap(markerView),
//            markerCardView.width,
//            markerView.height,
//            true
//        )
        //display image from drawable to image view

//        var bitmapRaw = AppCompatResources.getDrawable(this, R.drawable.anchor_gg_marker_custom)


//        val bitmap = view.drawToBitmap()
//        binding.ivLogo.setImageBitmap(bitmap)
//        binding.btnLogout.setOnClickListener {
//            authService.logout()
//            val intent = Intent(this, MainActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//            startActivity(intent)
//        }
    }
}