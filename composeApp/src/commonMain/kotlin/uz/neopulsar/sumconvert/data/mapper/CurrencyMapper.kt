package uz.neopulsar.sumconvert.data.mapper

import uz.neopulsar.sumconvert.data.CbuCurrencyDto
import uz.neopulsar.sumconvert.domain.model.Currency

fun CbuCurrencyDto.toDomain(): Currency {
    // 1. Parse Rate safely. Handle "12 500.00" or "12,500.00"
    // Remove spaces and replace comma with dot if needed (though CBU uses dots)
    val cleanRate = this.rate
        .replace(" ", "")
        .replace(",", ".")
        .toDoubleOrNull() ?: 0.0

    // 2. Parse Nominal safely
    val cleanNominal = this.nominal
        .replace(" ", "")
        .toIntOrNull() ?: 1

    return Currency(
        code = this.ccy,
        name = this.nameUz, // We prefer Latin Uzbek name
        nominal = cleanNominal,
        rate = cleanRate,
        date = this.date
    )
}