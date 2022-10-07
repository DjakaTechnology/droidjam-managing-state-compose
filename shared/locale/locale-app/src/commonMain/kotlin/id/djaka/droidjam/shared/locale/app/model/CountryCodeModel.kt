package id.djaka.droidjam.shared.locale.app.model

import id.djaka.droidjam.shared.core.util.Parcelable
import id.djaka.droidjam.shared.core.util.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class CountryCodeModel(
    @SerialName("name")
    val name: String,
    @SerialName("code")
    val code: String,
    @SerialName("emoji")
    val emoji: String,
    @SerialName("unicode")
    val unicode: String,
    @SerialName("image")
    val image: String
) : Parcelable