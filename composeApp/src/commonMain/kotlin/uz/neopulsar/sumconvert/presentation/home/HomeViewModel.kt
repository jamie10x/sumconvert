package uz.neopulsar.sumconvert.presentation.home

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.neopulsar.sumconvert.data.local.HistoryStorage
import uz.neopulsar.sumconvert.domain.model.HistoryItem

class HomeViewModel(
    private val historyStorage: HistoryStorage
) : ScreenModel {

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    // Called when screen becomes visible
    fun refreshHistory() {
        _history.value = historyStorage.getHistory()
    }
}