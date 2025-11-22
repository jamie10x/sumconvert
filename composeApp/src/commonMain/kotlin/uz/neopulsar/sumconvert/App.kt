package uz.neopulsar.sumconvert

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import uz.neopulsar.sumconvert.di.appModule
import uz.neopulsar.sumconvert.di.platformModule
import uz.neopulsar.sumconvert.presentation.converter.ConverterScreen

@Composable
@Preview
fun App() {
    // Wrap everything in Koin
    KoinApplication(application = {
        modules(appModule, platformModule)
    }) {
        MaterialTheme {
            ConverterScreen()
        }
    }
}