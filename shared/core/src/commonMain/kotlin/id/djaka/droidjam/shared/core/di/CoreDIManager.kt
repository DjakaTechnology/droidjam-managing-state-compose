package id.djaka.droidjam.shared.core.di

import com.squareup.sqldelight.db.SqlDriver
import id.djaka.droidjam.database.DroidJamDB
import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchersImpl
import id.djaka.droidjam.shared.core.repository.PreferenceRepository
import id.djaka.droidjam.shared.core.util.createDBDriver

object CoreDIManager {
    val appComponent = CoreComponent()

    suspend fun init() {
        appComponent.sqlDriver = appComponent.sqliteDriverFactory.createDriver()
    }
}

class CoreComponent {
    val sqliteDriverFactory by lazy { createDBDriver() }

    lateinit var sqlDriver: SqlDriver

    val droidJamDB by lazy { DroidJamDB(sqlDriver) }

    val coroutineDispatchers by lazy { CoroutineDispatchersImpl() }

    val preferenceRepository by lazy { PreferenceRepository() }
}