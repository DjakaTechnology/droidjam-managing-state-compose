package id.djaka.droidjam.shared.locale.presentation.api.di

import id.djaka.droidjam.shared.locale.presentation.api.LocalePresenterProvider
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object LocalePresentationApiDIManager {

    lateinit var presenterProvider: LocalePresenterProvider

}