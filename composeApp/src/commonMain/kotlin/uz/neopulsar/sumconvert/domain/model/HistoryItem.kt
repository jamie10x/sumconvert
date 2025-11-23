package uz.neopulsar.sumconvert.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id: Long = 0,
    val fromCode: String, // e.g. USD
    val toCode: String,   // e.g. UZS
    val amountIn: String, // 100
    val amountOut: String, // 1 250 000
    val timestamp: String // Simple string for V1 (e.g., "14:20")
)