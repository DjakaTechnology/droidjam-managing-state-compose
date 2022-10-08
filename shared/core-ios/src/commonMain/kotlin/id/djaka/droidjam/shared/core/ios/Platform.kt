package id.djaka.droidjam.shared.core.ios

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform