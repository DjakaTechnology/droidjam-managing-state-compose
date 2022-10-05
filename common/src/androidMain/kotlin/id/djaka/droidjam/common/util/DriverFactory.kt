package id.djaka.droidjam.common.util

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import id.djaka.droidjam.common.di.CoreAndroidDIManager
import id.djaka.droidjam.database.DroidJamDB

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = DroidJamDB.Schema,
            context = context,
            name = "direct_wa.db"
        )
    }
}

actual fun createDBDriver(): DriverFactory {
    return DriverFactory(CoreAndroidDIManager.appComponent.app)
}