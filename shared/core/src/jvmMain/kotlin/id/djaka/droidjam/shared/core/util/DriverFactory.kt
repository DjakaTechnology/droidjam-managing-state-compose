package id.djaka.droidjam.shared.core.util

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import id.djaka.droidjam.database.DroidJamDB
import id.djaka.droidjam.shared.core.util.DriverFactory

actual class DriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DroidJamDB.Schema.create(driver)
        return driver
    }
}

actual fun createDBDriver(): DriverFactory {
    return DriverFactory()
}