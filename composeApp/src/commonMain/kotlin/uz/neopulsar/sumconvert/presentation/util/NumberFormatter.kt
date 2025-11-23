package uz.neopulsar.sumconvert.presentation.util

import kotlin.math.roundToInt

fun formatNumber(value: Double): String {
    if (value == 0.0) return "0"

    // 1. Handle very large numbers (avoid 1.2E7) by converting to Long if it's huge
    // If value > 10 million, we usually don't care about cents in UZS
    if (value > 10_000_000) {
        val asLong = value.toLong()
        return formatWithSpaces(asLong.toString())
    }

    // 2. Standard rounding to 2 decimals
    // We use a trick to round to 2 decimals in KMP
    val rounded = (value * 100.0).roundToInt() / 100.0

    // Convert to string. Note: small Doubles won't use scientific notation.
    val str = rounded.toString()

    val parts = str.split('.')
    val integerPart = parts[0]
    val fractionPart = if (parts.size > 1) parts[1] else ""

    val formattedInt = formatWithSpaces(integerPart)

    // If fraction is "0" or empty, just return integer part
    return if (fractionPart.isEmpty() || fractionPart == "0") {
        formattedInt
    } else {
        "$formattedInt.$fractionPart"
    }
}

// Helper to add spaces: 1234567 -> 1 234 567
private fun formatWithSpaces(numberStr: String): String {
    var result = ""
    var count = 0
    for (i in numberStr.length - 1 downTo 0) {
        result = numberStr[i] + result
        count++
        if (count == 3 && i > 0) {
            result = " $result"
            count = 0
        }
    }
    return result
}