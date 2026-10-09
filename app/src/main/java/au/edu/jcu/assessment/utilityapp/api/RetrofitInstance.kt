package au.edu.jcu.assessment.utilityapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that creates and provides a single Retrofit client instance.
 */
object RetrofitInstance {

    private const val BASE_URL = "https://api.frankfurter.app/"

    val api: CurrencyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)
    }
}