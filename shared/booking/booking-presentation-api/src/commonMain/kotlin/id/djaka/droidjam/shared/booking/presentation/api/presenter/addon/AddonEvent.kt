package id.djaka.droidjam.shared.booking.presentation.api.presenter.addon

sealed interface AddonEvent {
    class CheckItem(val index: Int, val isChecked: Boolean) : AddonEvent
}