package com.example.thestudents.data.injection

import com.example.thestudents.data.datasource.services.InscripcionRetrofitService
import com.example.thestudents.data.datasource.services.ReviewRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent:: class)
object AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService {
        return retrofit.create(ReviewRetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideInscripcionRetrofitService(retrofit: Retrofit): InscripcionRetrofitService {
        return retrofit.create(InscripcionRetrofitService::class.java)
    }
}