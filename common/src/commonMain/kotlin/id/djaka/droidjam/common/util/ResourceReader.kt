package id.djaka.droidjam.common.util

expect class ResourceReader() {
    fun readResource(name: String): String
}