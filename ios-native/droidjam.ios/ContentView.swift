//
//  ContentView.swift
//  droidjam.ios
//
//  Created by Djaka Pradana Jaya on 08/10/22.
//

import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var viewModel = CountryPickerViewModel()
    @State var query: String = ""
    
    var body: some View {
        let state = viewModel.state
        
        VStack(alignment: .leading, spacing: 12) {
            Text("Select Country").bold()
            
            if let selectedCountryDisplay = state.selectedCountryDisplay {
                Text(selectedCountryDisplay)
            }
            
            TextField("Search ...", text: $query)
                .padding(8)
                .background(Color(.systemGray6))
                .cornerRadius(8)
                .onChange(of: query) { query in
                    viewModel.sendQueryChange(query: query)
                    
                }
            
            if state.isLoading {
                HStack {
                    Text("Loading...")
                        
                    ProgressView()
                }
                Spacer()
                
            } else if let emptyState = state.emptyStateMessage {
                
                Text(emptyState.message)
                Spacer()
                
            } else if let items = state.countryList {
                
                ScrollView {
                    LazyVStack(alignment: .leading, spacing: 12.0) {
                        ForEach((0...(items.endIndex - 1)), id: \.self) { index in
                            let item = items[index]
                            
                            if item is CountryPickerItem.Divider {
                                Divider()
                            } else if let item = item as? CountryPickerItem.Header {
                                Text(item.header)
                                    .bold()
                            } else if let item = item as? CountryPickerItem.Picker {
                                HStack {
                                    Text(item.item.emoji + " " + item.item.name)
                                    Spacer()
                                    Text(item.item.code)
                                        .opacity(0.3)
                                }.onTapGesture {
                                    viewModel.sendClick(item: item.item)
                                }
                            }
                        }
                    }
                }
                
            }
        }.padding(.horizontal, 12).onChange(of: state.searchBox) { searchBox in
            query = searchBox
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
