package id.djaka.droidjam.common.util

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import id.djaka.droidjam.database.DroidJamDB

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        DroidJamDB.Schema.create(driver)
        return driver
    }
}

actual fun createDBDriver(): DriverFactory {
    return DriverFactory()
}