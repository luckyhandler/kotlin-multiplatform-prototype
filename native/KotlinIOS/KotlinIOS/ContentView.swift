//
//  ContentView.swift
//  KotlinIOS
//
//  Created by Nino Handler on 08.01.20.
//  Copyright © 2020 Nino Handler. All rights reserved.
//

import Combine
import SwiftUI
import SharedCode


struct ContentView: View {
    @ObservedObject var viewModel = AstronautsViewModel.init()
    var body: some View {
        NavigationView {
            List(viewModel.astronauts, id: \.name) { astronaut in
                PersonView(astronaut: astronaut)
            }
            .navigationBarTitle(Text("Astronauts on ISS"), displayMode: .large)
        }
    }
}

struct PersonView : View {
    var astronaut: Astronaut
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(astronaut.name).font(.headline)
                Text(astronaut.craft).font(.subheadline)
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
