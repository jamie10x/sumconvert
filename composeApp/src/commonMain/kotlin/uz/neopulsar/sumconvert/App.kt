package uz.neopulsar.sumconvert

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.neopulsar.sumconvert.domain.model.AppCategory
import uz.neopulsar.sumconvert.presentation.converter.ConverterScreen
import uz.neopulsar.sumconvert.presentation.home.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        onCategoryClick = { category ->
                            when (category) {
                                AppCategory.CURRENCY -> navController.navigate("converter/currency")
                                else -> { /* Todo */ }
                            }
                        }
                    )
                }

                composable("converter/currency") {
                    ConverterScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}