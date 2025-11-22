package uz.neopulsar.sumconvert.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val code: String,      // USD
    val name: String,      // AQSH dollari
    val nominal: Int,      // 1
    val rate: Double,      // 12450.50 (Clean double)
    val date: String       // 20.11.2025
) {
    // Helper for generic UZS
    companion object {
        val UZS = Currency(
            code = "UZS",
            name = "O'zbek so'mi",
            nominal = 1,
            rate = 1.0,
            date = ""
        )
    }
}