package id.djaka.droidjam.common.ui.country_picker_old

import id.djaka.droidjam.common.model.CountryCodeModel

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