package uz.neopulsar.sumconvert.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import uz.neopulsar.sumconvert.data.CbuCurrencyDto
import uz.neopulsar.sumconvert.data.local.RateStorage
import uz.neopulsar.sumconvert.data.mapper.toDomain
import uz.neopulsar.sumconvert.domain.model.Currency

class CurrencyRepository(
    private val client: HttpClient,
    private val storage: RateStorage
) {
    private val apiUrl = "https://cbu.uz/oz/arkhiv-kursov-valyut/json/"

    // Returns a Result containing the list.
    // Logic: Try Network -> Save -> Return.
    // If Network Fails -> Load Cache.
    suspend fun getRates(forceRefresh: Boolean = false): Result<List<Currency>> {
        return try {
            if (!forceRefresh && storage.hasRates()) {
                // Determine if we want to aggressively rely on cache or check network.
                // For now, let's try network first to always be fresh,
                // but if we are offline, catch block handles it.
            }

            // 1. Network Call
            val dtos: List<CbuCurrencyDto> = client.get(apiUrl).body()

            // 2. Map & Add UZS Base
            val domainRates = dtos.map { it.toDomain() }.toMutableList()
            domainRates.add(0, Currency.UZS) // Add UZS at the top

            // 3. Save to Cache
            storage.saveRates(domainRates)

            Result.success(domainRates)
        } catch (e: Exception) {
            // 4. Fallback to Cache
            val cached = storage.getRates()
            if (cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                Result.failure(e)
            }
        }
    }
}