//
//  droidjam_iosApp.swift
//  droidjam.ios
//
//  Created by Djaka Pradana Jaya on 08/10/22.
//

import SwiftUI
import Shared
import RxSwift

@MainActor class ApplicationState: ObservableObject {
    @Published var isInitialized = false
    let disposeBag = DisposeBag()
    
    init() {
        createSingle(scope: SharedModule.shared.scope, suspendWrapper: SharedModule.shared.doInit())
            .subscribe(onSuccess: { _ in
                self.isInitialized = true
            }).disposed(by: self.disposeBag)
    }
}


@main
struct droidjam_iosApp: App {
    @StateObject private var state = ApplicationState()
    
    var body: some Scene {
        WindowGroup {
            if state.isInitialized {
                ContentView()
            }
        }
    }
}
