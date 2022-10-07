package id.djaka.droidjam.shared.core.util

expect interface Parcelable

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize()