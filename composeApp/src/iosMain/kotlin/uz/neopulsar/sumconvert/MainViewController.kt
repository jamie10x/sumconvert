package uz.neopulsar.sumconvert

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.compose.KoinApplication
import uz.neopulsar.sumconvert.di.appModule
import uz.neopulsar.sumconvert.di.platformModule

fun MainViewController() = ComposeUIViewController {
    KoinApplication(application = {
        modules(appModule, platformModule)
    }) {
        App()
    }
}