package uz.neopulsar.sumconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import uz.neopulsar.sumconvert.di.appModule
import uz.neopulsar.sumconvert.di.platformModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            KoinApplication(application = {
                androidContext(this@MainActivity)
                modules(appModule, platformModule)
            }) {
                App() // App is inside KoinApplication, so koinInject() works automatically
            }
        }
    }
}