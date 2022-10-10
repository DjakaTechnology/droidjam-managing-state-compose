//
//  CountryPickerViewModel.swift
//  droidjam.ios
//
//  Created by Djaka Pradana Jaya on 08/10/22.
//

import Foundation
import Shared
import RxSwift

extension ContentView {
    @MainActor class CountryPickerViewModel: ObservableObject {
        @Published var state: CountryPickerModel
        let presenter: PresenterWrapper<CountryPickerEvent, CountryPickerModel>
        let disposeBag = DisposeBag()
    
    
        init() {
            state = CountryPickerModel.companion.empty()
            presenter = PresenterWrapper(
                presenter: SharedModule.Locale.shared.getPresenterProvider().provideCountryPickerPresenter()
            )
            
            createObservable(scope: presenter.coroutineScope, flowWrapper: presenter.listen())
                .subscribe { item in
                    self.state = item
                }.disposed(by: self.disposeBag)
        }
        
        private func onInitialized() {
            
        }
        
        func event(event: CountryPickerEvent) {
            presenter.event(value: event)
        }
        
        func sendQueryChange(query: String) {
            event(event: CountryPickerEvent.SearchBoxChanged(query: query))
        }
        
        func sendClick(item: CountryCodeModel) {
            event(event: CountryPickerEvent.ItemClicked(item: item))
        }
    }

}
