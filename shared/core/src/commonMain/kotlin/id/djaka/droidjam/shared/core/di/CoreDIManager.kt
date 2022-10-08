package id.djaka.droidjam.shared.core.di

import id.djaka.droidjam.database.DroidJamDB
import id.djaka.droidjam.shared.core.framework.dispatcher.CoroutineDispatchersImpl
import id.djaka.droidjam.shared.core.repository.PreferenceRepository
import id.djaka.droidjam.shared.core.util.createDBDriver

object CoreDIManager {
    val appComponent = CoreComponent()

}

class CoreComponent {
    val sqliteDriver by lazy { createDBDriver() }

    val droidJamDB by lazy { DroidJamDB(sqliteDriver.createDriver()) }

    val coroutineDispatchers by lazy { CoroutineDispatchersImpl() }

    val preferenceRepository by lazy { PreferenceRepository() }
}