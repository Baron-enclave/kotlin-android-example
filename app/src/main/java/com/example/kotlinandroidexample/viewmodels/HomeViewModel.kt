package com.example.kotlinandroidexample.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kotlinandroidexample.models.HPCharacter
import com.example.kotlinandroidexample.services.HPCharacterService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val hpCharacterService: HPCharacterService) :
    ViewModel() {
    val charactersObs: Observable<HomePageState>

    init {
        charactersObs = hpCharacterService.getCharacters()
            .map<HomePageState> { HomePageState.Success(it) }
            .onErrorReturn { HomePageState.Failure(it.message ?: "Unknown Error") }
            .startWithItem(HomePageState.Loading)
            .subscribeOn(Schedulers.io())
    }


    sealed class HomePageState {
        data class Success(val characters: List<HPCharacter>) : HomePageState()
        class Failure(val message: String) : HomePageState()
        data object Loading : HomePageState()
    }
}