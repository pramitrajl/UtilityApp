package au.edu.jcu.assessment.utilityapp.viewmodel
/**
 * Data class representing all UI state variables for the Currency Converter screen.
 */
data class CurrencyUiState(
    val amountInput: String = "100",
    val fromCurrency: String = "USD",
    val toCurrency: String = "AUD",
    val availableCurrencies: List<String> = listOf("USD", "AUD", "EUR", "GBP", "JPY", "CAD", "INR", "NZD", "SGD"),
    val rates: Map<String, Double> = emptyMap(),
    val convertedResult: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)