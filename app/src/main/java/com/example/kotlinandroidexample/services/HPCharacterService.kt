package com.example.kotlinandroidexample.services

import com.example.kotlinandroidexample.models.HPCharacter
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface HPCharacterService {
    @GET("characters")
    fun getCharacters(): Observable<List<HPCharacter>>
}