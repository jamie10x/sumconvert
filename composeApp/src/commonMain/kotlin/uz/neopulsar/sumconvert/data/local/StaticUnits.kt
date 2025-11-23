package uz.neopulsar.sumconvert.data.local

import uz.neopulsar.sumconvert.domain.model.AppCategory

data class GenericUnit(
    val id: String,
    val name: String,
    val factor: Double // Relative to base unit
)

object StaticUnits {

    private val lengthUnits = listOf(
        GenericUnit("mm", "Millimetr", 0.001),
        GenericUnit("cm", "Santimetr", 0.01),
        GenericUnit("m", "Metr", 1.0), // BASE
        GenericUnit("km", "Kilometr", 1000.0),
        GenericUnit("in", "Dyuym (Inch)", 0.0254),
        GenericUnit("ft", "Fut (Feet)", 0.3048),
        GenericUnit("yd", "Yard", 0.9144),
        GenericUnit("mi", "Mil (Mile)", 1609.34)
    )

    private val weightUnits = listOf(
        GenericUnit("mg", "Milligram", 0.000001),
        GenericUnit("g", "Gramm", 0.001),
        GenericUnit("kg", "Kilogram", 1.0), // BASE
        GenericUnit("oz", "Unsiya (Ounce)", 0.0283495),
        GenericUnit("lb", "Funt (Pound)", 0.453592),
        GenericUnit("t", "Tonna", 1000.0)
    )

    private val fuelUnits = listOf(
        GenericUnit("l", "Litr", 1.0), // BASE
        GenericUnit("ml", "Millilitr", 0.001),
        GenericUnit("gal_us", "Gallon (US)", 3.78541),
        GenericUnit("gal_uk", "Gallon (UK)", 4.54609),
        GenericUnit("bbl", "Barrel (Neft)", 158.987)
    )

    private val goldUnits = listOf(
        GenericUnit("g", "Gramm", 1.0), // BASE
        GenericUnit("kg", "Kilogram", 1000.0),
        GenericUnit("oz_t", "Troy Unsiya (Troy oz)", 31.1035),
        GenericUnit("tola", "Tola (Hind)", 11.6638),
        GenericUnit("carat", "Karat (ct)", 0.2)
    )


    fun getUnits(category: AppCategory): List<GenericUnit> {
        return when(category) {
            AppCategory.CONSTRUCTION -> lengthUnits
            AppCategory.MATH -> lengthUnits
            AppCategory.FOOD -> weightUnits
            AppCategory.FUEL -> fuelUnits
            AppCategory.GOLD -> goldUnits
            else -> emptyList()
        }
    }

    fun getDefaultFrom(category: AppCategory): GenericUnit? = when(category) {
        AppCategory.GOLD -> goldUnits.find { it.id == "g" }
        AppCategory.FUEL -> fuelUnits.find { it.id == "l" }
        else -> getUnits(category).getOrNull(2) ?: getUnits(category).firstOrNull()
    }

    fun getDefaultTo(category: AppCategory): GenericUnit? = when(category) {
        AppCategory.GOLD -> goldUnits.find { it.id == "oz_t" }
        AppCategory.FUEL -> fuelUnits.find { it.id == "gal_us" }
        else -> getUnits(category).firstOrNull()
    }
}