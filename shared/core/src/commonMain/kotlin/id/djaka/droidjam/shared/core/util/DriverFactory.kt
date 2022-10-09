package id.djaka.droidjam.shared.core.util

import com.squareup.sqldelight.db.SqlDriver

expect class DriverFactory {
    suspend fun createDriver(): SqlDriver
}

expect fun createDBDriver(): DriverFactory