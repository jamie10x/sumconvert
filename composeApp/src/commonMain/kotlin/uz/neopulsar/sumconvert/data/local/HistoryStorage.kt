package uz.neopulsar.sumconvert.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uz.neopulsar.sumconvert.domain.model.HistoryItem

class HistoryStorage(private val settings: Settings) {

    companion object {
        private const val KEY_HISTORY = "conversion_history"
        private const val MAX_ITEMS = 10
    }

    fun getHistory(): List<HistoryItem> {
        val json = settings.getStringOrNull(KEY_HISTORY) ?: return emptyList()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveItem(item: HistoryItem) {
        val currentList = getHistory().toMutableList()

        // Remove duplicate pair if it exists at the top to avoid spam
        if (currentList.isNotEmpty()) {
            val top = currentList.first()
            if (top.fromCode == item.fromCode && top.toCode == item.toCode) {
                currentList.removeAt(0)
            }
        }

        // Add new item to TOP
        currentList.add(0, item)

        // Trim to Max Size
        if (currentList.size > MAX_ITEMS) {
            currentList.removeAt(currentList.lastIndex)
        }

        settings[KEY_HISTORY] = Json.encodeToString(currentList)
    }

    fun clear() {
        settings.remove(KEY_HISTORY)
    }
}