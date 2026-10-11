package au.edu.jcu.assessment.utilityapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * ViewModel managing currency conversion logic, API network calls, and UI state.
 */
class CurrencyViewModel : ViewModel() {

    // Internal mutable state flow (only ViewModel can edit)
    private val _uiState = MutableStateFlow(CurrencyUiState())

    // Public read-only state flow (UI screens can read, but cannot modify directly)
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        // Automatically fetch exchange rates when ViewModel is first created
        fetchExchangeRates()
    }

    /**
     * Fetches live exchange rates from the API for the current 'from' base currency.
     */
    fun fetchExchangeRates() {
        val base = _uiState.value.fromCurrency

        viewModelScope.launch {
            // 1. Set loading to true
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 2. Call API on background thread
                val response = RetrofitInstance.api.getExchangeRates(base)

                // 3. Update rates in UI state
                _uiState.update { state ->
                    val ratesMap = response.rates.toMutableMap()
                    ratesMap[base] = 1.0 // Base currency conversion with itself is 1.0

                    state.copy(
                        rates = ratesMap,
                        isLoading = false
                    )
                }

                // 4. Trigger conversion calculation
                calculateConversion()

            } catch (e: Exception) {
                // 4. Handle errors (e.g. no internet)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to fetch rates: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    /**
     * Calculates the converted amount based on input amount and selected target rate.
     */
    fun calculateConversion() {
        val amount = _uiState.value.amountInput.toDoubleOrNull()
        val toRate = _uiState.value.rates[_uiState.value.toCurrency]

        if (amount != null && toRate != null) {
            val result = amount * toRate
            val formatted = String.format(Locale.US, "%.2f %s", result, _uiState.value.toCurrency)
            _uiState.update { it.copy(convertedResult = formatted) }
        } else {
            _uiState.update { it.copy(convertedResult = "--") }
        }
    }
}