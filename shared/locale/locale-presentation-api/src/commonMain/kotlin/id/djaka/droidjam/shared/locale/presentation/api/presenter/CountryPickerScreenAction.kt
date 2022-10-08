package id.djaka.droidjam.shared.locale.presentation.api.presenter

import id.djaka.droidjam.shared.locale.presentation.api.model.CountryCodeModel

interface CountryPickerScreenAction {
    fun onSearchBoxChanged(query: String)

    fun onItemClicked(item: CountryCodeModel)

    companion object {
        fun empty() = object : CountryPickerScreenAction {
            override fun onSearchBoxChanged(query: String) {}

            override fun onItemClicked(item: CountryCodeModel) {}

        }
    }
}