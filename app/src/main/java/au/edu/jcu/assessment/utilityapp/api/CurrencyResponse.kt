package au.edu.jcu.assessment.utilityapp.api


import com.google.gson.annotations.SerializedName

/**
 * Data class representing the exchange rate response from Frankfurter API.
 * @param amount Base amount requested (usually 1.0)
 * @param base Base currency code (e.g., "USD")
 * @param date Date of exchange rates
 * @param rates Map of currency code (e.g., "AUD") to exchange rate multiplier (e.g., 1.54)
 */
data class CurrencyResponse(
    @SerializedName("amount")
    val amount: Double,

    @SerializedName("base")
    val base: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("rates")
    val rates: Map<String, Double>
)