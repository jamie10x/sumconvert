package uz.neopulsar.sumconvert

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform