package com.realityexpander.androidcoroutinesretrofit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.realityexpander.androidcoroutinesretrofit.model.CountriesService
import com.realityexpander.androidcoroutinesretrofit.model.Country
import kotlinx.coroutines.*

class ListViewModel: ViewModel() {

    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()

    val countriesService = CountriesService.getCountriesService()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception: ${throwable.localizedMessage}")
    }

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        loading.value = true

//        val dummyData = generateDummyCountries()
//        countries.value = dummyData

        job = CoroutineScope(Dispatchers.IO).launch {
            val response = countriesService.getCountries()
            withContext(Dispatchers.Main) {
                if(response.isSuccessful) {
                    countries.value = response.body()
                    countryLoadError.value = null
                    loading.value = false
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }

        countryLoadError.value = ""
        loading.value = false
    }

//    private fun generateDummyCountries(): List<Country> {
//        val countries = arrayListOf<Country>()
//        countries.add(Country("dummyCountry1",  "dummyCapital1",""))
//        countries.add(Country("dummyCountry2",  "dummyCapital2",""))
//        countries.add(Country("dummyCountry3",  "dummyCapital3",""))
//        countries.add(Country("dummyCountry4",  "dummyCapital4",""))
//        countries.add(Country("dummyCountry5",  "dummyCapital5",""))
//        return countries
//    }

    private fun onError(message: String) {
        countryLoadError.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}