package id.djaka.droidjam.common.model

import id.djaka.droidjam.common.util.Parcelable
import id.djaka.droidjam.common.util.Parcelize
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