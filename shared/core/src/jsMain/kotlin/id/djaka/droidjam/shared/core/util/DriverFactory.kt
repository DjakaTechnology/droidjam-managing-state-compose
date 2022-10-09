package id.djaka.droidjam.shared.core.util

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import id.djaka.droidjam.database.DroidJamDB
import kotlinx.coroutines.await

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return initSqlDriver(DroidJamDB.Schema).await()
    }
}

actual fun createDBDriver(): DriverFactory {
    return DriverFactory()
}