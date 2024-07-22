package com.example.kotlinandroidexample.repositories

import com.example.kotlinandroidexample.models.ExploreLocalDataSource
import com.example.kotlinandroidexample.models.ExploreRemoteDataSource
import javax.inject.Inject

class ExploreRepository @Inject constructor(
    val exploreRemoteDataSource: ExploreRemoteDataSource,
    val exploreLocalDataSource: ExploreLocalDataSource
) {
}