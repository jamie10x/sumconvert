package uz.neopulsar.sumconvert.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uz.neopulsar.sumconvert.domain.model.Currency

class RateStorage(private val settings: Settings) {

    companion object {
        private const val KEY_RATES = "cached_rates_json"
        private const val KEY_LAST_UPDATED = "last_updated_timestamp"
    }

    fun saveRates(rates: List<Currency>) {
        val jsonString = Json.encodeToString(rates)
        settings[KEY_RATES] = jsonString
        settings[KEY_LAST_UPDATED] = 0L // You can store real timestamp here later
    }

    fun getRates(): List<Currency> {
        val jsonString = settings.getStringOrNull(KEY_RATES) ?: return emptyList()
        return try {
            Json.decodeFromString(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun hasRates(): Boolean = settings.getStringOrNull(KEY_RATES) != null
}