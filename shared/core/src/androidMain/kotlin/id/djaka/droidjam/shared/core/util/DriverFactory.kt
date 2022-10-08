package id.djaka.droidjam.shared.core.util

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import id.djaka.droidjam.shared.core.di.CoreAndroidDIManager
import id.djaka.droidjam.database.DroidJamDB
import id.djaka.droidjam.shared.core.util.DriverFactory

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = DroidJamDB.Schema,
            context = context,
            name = "droidjam.db"
        )
    }
}

actual fun createDBDriver(): DriverFactory {
    return id.djaka.droidjam.shared.core.util.DriverFactory(CoreAndroidDIManager.appComponent.app)
}