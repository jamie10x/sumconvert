package uz.neopulsar.sumconvert.di

import androidx.compose.ui.text.font.FontVariation.Settings
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import uz.neopulsar.sumconvert.data.local.RateStorage
import uz.neopulsar.sumconvert.data.repository.CurrencyRepository
import uz.neopulsar.sumconvert.presentation.converter.ConverterViewModel

val appModule = module {
    // 1. Network
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
        }
    }

    // 2. Storage
    single { Settings() } // Default Platform Settings
    single { RateStorage(get()) }

    // 3. Repository
    single { CurrencyRepository(get(), get()) }

    // 4. ViewModel (We are about to create this)
    factory { ConverterViewModel(get()) }
}