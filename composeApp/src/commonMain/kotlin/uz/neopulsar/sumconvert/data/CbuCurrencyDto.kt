package uz.neopulsar.sumconvert.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CbuCurrencyDto(
    val id: Int,
    @SerialName("Code") val code: String,
    @SerialName("Ccy") val ccy: String,
    @SerialName("CcyNm_RU") val nameRu: String,
    @SerialName("CcyNm_UZ") val nameUz: String,
    @SerialName("CcyNm_UZC") val nameUzCyrillic: String,
    @SerialName("CcyNm_EN") val nameEn: String,
    @SerialName("Nominal") val nominal: String,
    @SerialName("Rate") val rate: String,
    @SerialName("Diff") val diff: String,
    @SerialName("Date") val date: String
)