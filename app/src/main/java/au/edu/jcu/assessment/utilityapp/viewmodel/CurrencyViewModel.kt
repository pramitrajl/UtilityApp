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

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        fetchExchangeRates()
    }

    /**
     * Fetches live exchange rates from the API for the current 'from' base currency.
     */
    fun fetchExchangeRates() {
        val base = _uiState.value.fromCurrency

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = RetrofitInstance.api.getExchangeRates(base)

                _uiState.update { state ->
                    val ratesMap = response.rates.toMutableMap()
                    ratesMap[base] = 1.0

                    state.copy(
                        rates = ratesMap,
                        isLoading = false
                    )
                }

                calculateConversion()

            } catch (e: Exception) {
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

    // ==========================================
    // USER INTERACTION FUNCTIONS
    // ==========================================

    /**
     * Called when user types a new amount in the text field.
     */
    fun onAmountChanged(newAmount: String) {
        _uiState.update { it.copy(amountInput = newAmount) }
        calculateConversion()
    }

    /**
     * Called when user selects a new source currency in the 'From' dropdown.
     */
    fun onFromCurrencyChanged(newFrom: String) {
        _uiState.update { it.copy(fromCurrency = newFrom) }
        fetchExchangeRates() // Re-fetch rates for the new base currency
    }

    /**
     * Called when user selects a new target currency in the 'To' dropdown.
     */
    fun onToCurrencyChanged(newTo: String) {
        _uiState.update { it.copy(toCurrency = newTo) }
        calculateConversion()
    }

    /**
     * Swaps 'From' and 'To' currencies when user taps the swap icon button.
     */
    fun swapCurrencies() {
        _uiState.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency
            )
        }
        fetchExchangeRates()
    }
}