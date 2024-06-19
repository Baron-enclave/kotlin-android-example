package com.example.kotlinandroidexample.views.home

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinandroidexample.databinding.ActivityHomeBinding
import com.example.kotlinandroidexample.helpers.ApiHelper
import com.example.kotlinandroidexample.models.HPCharacter
import com.example.kotlinandroidexample.services.HPCharacterService
import com.example.kotlinandroidexample.viewmodels.HomeViewModel
import com.example.kotlinandroidexample.views.CharacterDetailActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

class HomeActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding
    private var disposable: Disposable? = null
    private lateinit var characters: List<HPCharacter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel =
            HomeViewModel(ApiHelper().getInstance().create(HPCharacterService::class.java))

        val progressBar = binding.progressBar
        val recyclerView = binding.rcView
        recyclerView.layoutManager = LinearLayoutManager(this)

        disposable = homeViewModel.charactersObs
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when (it) {
                        is HomeViewModel.HomePageState.Success -> {
                            characters = it.characters
                            progressBar.visibility = ProgressBar.GONE
                            recyclerView.visibility = ProgressBar.VISIBLE
                        }

                        is HomeViewModel.HomePageState.Failure -> {
                            progressBar.visibility = ProgressBar.GONE
                            recyclerView.visibility = ProgressBar.GONE
                        }

                        is HomeViewModel.HomePageState.Loading -> {
                            progressBar.visibility = ProgressBar.VISIBLE
                            recyclerView.visibility = ProgressBar.GONE
                        }
                    }
                },
                {
                    it.printStackTrace()
                },
                {
                    recyclerView.adapter = HPCharactersAdapter(characters) {
                        onClick(it)
                    }
                },
            )


    }

    private fun onClick(character: HPCharacter) {
        val intent = Intent(this, CharacterDetailActivity::class.java)
        intent.putExtra("character", character)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}