package uz.neopulsar.sumconvert.presentation.converter

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.neopulsar.sumconvert.data.local.HistoryStorage
import uz.neopulsar.sumconvert.data.repository.CurrencyRepository
import uz.neopulsar.sumconvert.domain.model.Currency
import uz.neopulsar.sumconvert.domain.model.HistoryItem
import uz.neopulsar.sumconvert.presentation.util.formatNumber
import kotlin.random.Random

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
    private val repository: CurrencyRepository,
    private val historyStorage: HistoryStorage
) : ScreenModel {

    private val _state = MutableStateFlow(ConverterUiState(isLoading = true))
    val state: StateFlow<ConverterUiState> = _state.asStateFlow()
    private var saveJob: Job? = null


    init {
        loadRates()
    }

    fun loadRates() {
        screenModelScope.launch {
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

        val valueInUzs = amount * (from.rate / from.nominal)
        val valueInTarget = valueInUzs / (to.rate / to.nominal)

        val formatted = formatNumber(valueInTarget)
        _state.value = _state.value.copy(resultAmount = formatted)

        // TRIGGER AUTO SAVE
        triggerAutoSave(
            fromCode = from.code,
            toCode = to.code,
            inAmt = s.inputAmount,
            outAmt = formatted
        )
    }

    private fun triggerAutoSave(fromCode: String, toCode: String, inAmt: String, outAmt: String) {
        // Don't save empty or zero conversions
        if (inAmt == "0" || inAmt.isEmpty()) return

        // Cancel previous timer
        saveJob?.cancel()

        // Start new timer: Wait 2 seconds after user stops typing
        saveJob = screenModelScope.launch {
            delay(2000)

            val item = HistoryItem(
                id = Random.nextLong(),
                fromCode = fromCode,
                toCode = toCode,
                amountIn = inAmt,
                amountOut = outAmt,
                timestamp = "Bugun" // Simply "Today" for V1
            )
            historyStorage.saveItem(item)
            println("History Saved: $inAmt $fromCode")
        }
    }
}