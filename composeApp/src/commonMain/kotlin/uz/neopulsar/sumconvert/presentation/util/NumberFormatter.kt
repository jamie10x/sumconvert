package uz.neopulsar.sumconvert.presentation.util

import kotlin.math.roundToInt

fun formatNumber(value: Double): String {
    if (value == 0.0) return "0"

    // Round to 2 decimals
    val rounded = (value * 100.0).roundToInt() / 100.0
    val str = rounded.toString() // "1234.56"

    val parts = str.split('.')
    val integerPart = parts[0]
    val fractionPart = if (parts.size > 1) parts[1] else ""

    // Add spaces
    var formattedInt = ""
    var count = 0
    for (i in integerPart.length - 1 downTo 0) {
        formattedInt = integerPart[i] + formattedInt
        count++
        if (count == 3 && i > 0) {
            formattedInt = " $formattedInt"
            count = 0
        }
    }

    return if (fractionPart.isEmpty() || fractionPart == "0") {
        formattedInt
    } else {
        "$formattedInt.$fractionPart"
    }
}