package uz.neopulsar.sumconvert.presentation.generic

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.neopulsar.sumconvert.data.local.GenericUnit
import uz.neopulsar.sumconvert.data.local.StaticUnits
import uz.neopulsar.sumconvert.domain.model.AppCategory
import uz.neopulsar.sumconvert.presentation.util.formatNumber

data class GenericUiState(
    val category: AppCategory = AppCategory.CONSTRUCTION,
    val units: List<GenericUnit> = emptyList(),
    val fromUnit: GenericUnit? = null,
    val toUnit: GenericUnit? = null,
    val input: String = "1",
    val output: String = ""
)

class GenericViewModel : ScreenModel {

    private val _state = MutableStateFlow(GenericUiState())
    val state = _state.asStateFlow()

    fun initCategory(categoryId: String) {
        // Find category enum from string ID
        val category = AppCategory.entries.find { it.id == categoryId } ?: return
        val units = StaticUnits.getUnits(category)

        _state.value = GenericUiState(
            category = category,
            units = units,
            fromUnit = StaticUnits.getDefaultFrom(category) ?: units.firstOrNull(),
            toUnit = StaticUnits.getDefaultTo(category) ?: units.lastOrNull(),
            input = "1"
        )
        calculate()
    }

    fun onAmountChanged(v: String) {
        if (v.all { it.isDigit() || it == '.' }) {
            _state.value = _state.value.copy(input = v)
            calculate()
        }
    }

    fun onFromChanged(u: GenericUnit) {
        _state.value = _state.value.copy(fromUnit = u)
        calculate()
    }

    fun onToChanged(u: GenericUnit) {
        _state.value = _state.value.copy(toUnit = u)
        calculate()
    }

    fun swap() {
        val oldFrom = _state.value.fromUnit
        val oldTo = _state.value.toUnit
        _state.value = _state.value.copy(fromUnit = oldTo, toUnit = oldFrom)
        calculate()
    }

    private fun calculate() {
        val s = _state.value
        val amount = s.input.toDoubleOrNull() ?: 0.0
        val from = s.fromUnit ?: return
        val to = s.toUnit ?: return

        // Formula: Amount * (FromFactor / ToFactor)
        val baseValue = amount * from.factor
        val finalValue = baseValue / to.factor

        _state.value = _state.value.copy(output = formatNumber(finalValue))
    }
}