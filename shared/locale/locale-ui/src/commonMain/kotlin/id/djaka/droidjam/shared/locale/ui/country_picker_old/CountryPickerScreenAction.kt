package id.djaka.droidjam.shared.locale.ui.country_picker_old

import id.djaka.droidjam.shared.locale.app.model.CountryCodeModel

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