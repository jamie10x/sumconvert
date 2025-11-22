package uz.neopulsar.sumconvert.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppCategory(
    val id: String,
    val nameUz: String,
    val icon: ImageVector
) {
    CURRENCY("currency", "Valyuta", Icons.Default.AttachMoney),
    FUEL("fuel", "Yoqilg'i", Icons.Default.LocalGasStation),
    GOLD("gold", "Oltin", Icons.Default.HorizontalSplit), // Placeholder
    FOOD("food", "Oziq-ovqat", Icons.Default.Restaurant),
    CONSTRUCTION("construction", "Qurilish", Icons.Default.Foundation),
    MATH("math", "Matematika", Icons.Default.Calculate),
    CUSTOM("custom", "Maxsus", Icons.Default.Edit)
}