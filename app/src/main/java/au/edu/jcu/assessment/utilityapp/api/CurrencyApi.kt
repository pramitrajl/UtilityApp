package au.edu.jcu.assessment.utilityapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    /**
     * Fetches exchange rates relative to a base currency.
     * Example URL generated: https://api.frankfurter.app/latest?from=USD
     */
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("from") baseCurrency: String
    ): CurrencyResponse
}