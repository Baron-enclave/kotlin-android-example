package com.example.kotlinandroidexample

import android.app.Application
import com.example.kotlinandroidexample.services.ExploreRetrofitService
import com.example.kotlinandroidexample.views.explore.ExploreFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ]
)
interface ApplicationComponent {
    fun inject(exploreFragment: ExploreFragment)
}


@Module
class NetworkModule {
    @Provides
    fun provideExploreRetrofitService(): ExploreRetrofitService {
        return Retrofit.Builder().baseUrl("https://example.com").build()
            .create(ExploreRetrofitService::class.java)
    }
}