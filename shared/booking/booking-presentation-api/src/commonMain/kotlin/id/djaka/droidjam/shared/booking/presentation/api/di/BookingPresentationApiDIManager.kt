package id.djaka.droidjam.shared.booking.presentation.api.di

import id.djaka.droidjam.shared.booking.presentation.api.BookingPresenterProvider
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object BookingPresentationApiDIManager {

    lateinit var presenterProvider: BookingPresenterProvider

}