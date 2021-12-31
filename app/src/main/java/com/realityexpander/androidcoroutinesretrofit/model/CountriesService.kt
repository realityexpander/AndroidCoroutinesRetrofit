package com.realityexpander.androidcoroutinesretrofit.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const private val BASE_URL = "https://raw.githubusercontent.com"

class CountriesService {
    
    fun getCountriesService(): CountriesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }
    
}