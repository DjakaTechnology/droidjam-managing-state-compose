package id.djaka.droidjam.common.util

import java.io.InputStreamReader

actual class ResourceReader {
    actual fun readResource(name: String): String {
        return javaClass.classLoader?.getResourceAsStream(name).use { stream ->
            InputStreamReader(stream).use { reader ->
                reader.readText()
            }
        }
    }
}