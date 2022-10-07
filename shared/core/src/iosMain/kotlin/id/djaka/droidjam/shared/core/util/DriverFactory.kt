package id.djaka.droidjam.shared.core.util

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import id.djaka.droidjam.database.DroidJamDB
import id.djaka.droidjam.shared.core.util.DriverFactory

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(DroidJamDB.Schema, "droidjam.db")
    }
}

actual fun createDBDriver(): DriverFactory {
    return id.djaka.droidjam.shared.core.util.DriverFactory()
}