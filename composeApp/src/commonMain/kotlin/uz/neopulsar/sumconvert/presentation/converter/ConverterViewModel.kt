package uz.neopulsar.sumconvert.presentation.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.neopulsar.sumconvert.data.repository.CurrencyRepository
import uz.neopulsar.sumconvert.domain.model.Currency

// 1. Define the UI State
data class ConverterUiState(
    val isLoading: Boolean = false,
    val rates: List<Currency> = emptyList(),
    val fromCurrency: Currency? = null,
    val toCurrency: Currency? = null,
    val inputAmount: String = "1",
    val resultAmount: String = "",
    val errorMessage: String? = null
)

// 2. The ViewModel
class ConverterViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConverterUiState(isLoading = true))
    val state: StateFlow<ConverterUiState> = _state.asStateFlow()

    init {
        loadRates()
    }

    fun loadRates() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            repository.getRates().fold(
                onSuccess = { list ->
                    // Set default: From USD to UZS if available, else first items
                    val usd = list.find { it.code == "USD" } ?: list.firstOrNull()
                    val uzs = list.find { it.code == "UZS" } ?: list.lastOrNull()

                    _state.value = _state.value.copy(
                        isLoading = false,
                        rates = list,
                        fromCurrency = usd,
                        toCurrency = uzs
                    )
                    calculate()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = "Error loading rates: ${error.message}"
                    )
                }
            )
        }
    }

    fun onAmountChanged(newAmount: String) {
        // Only allow numeric input (and one dot)
        if (newAmount.all { it.isDigit() || it == '.' }) {
            _state.value = _state.value.copy(inputAmount = newAmount)
            calculate()
        }
    }

    fun onFromChanged(currency: Currency) {
        _state.value = _state.value.copy(fromCurrency = currency)
        calculate()
    }

    fun onToChanged(currency: Currency) {
        _state.value = _state.value.copy(toCurrency = currency)
        calculate()
    }

    fun swapCurrencies() {
        val oldFrom = _state.value.fromCurrency
        val oldTo = _state.value.toCurrency
        _state.value = _state.value.copy(fromCurrency = oldTo, toCurrency = oldFrom)
        calculate()
    }

    private fun calculate() {
        val s = _state.value
        if (s.fromCurrency == null || s.toCurrency == null) return

        val amount = s.inputAmount.toDoubleOrNull() ?: 0.0
        val from = s.fromCurrency!!
        val to = s.toCurrency!!

        // Formula: (Amount * FromRate / FromNominal) / (ToRate / ToNominal)
        // Note: CBU rates are all relative to UZS (Nominal 1, Rate 1)

        val valueInUzs = amount * (from.rate / from.nominal)
        val valueInTarget = valueInUzs / (to.rate / to.nominal)

        // Format result (simple string format for now)
        // In real app, use a NumberFormatter
        val formatted = if (valueInTarget % 1.0 == 0.0) {
            valueInTarget.toLong().toString()
        } else {
            // Keep 2 decimal places manually
            val p = (valueInTarget * 100).toLong() / 100.0
            p.toString()
        }

        _state.value = _state.value.copy(resultAmount = formatted)
    }
}