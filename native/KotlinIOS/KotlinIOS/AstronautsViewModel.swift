//
//  AstronautsViewModel.swift
//  KotlinIOS
//
//  Created by Nino Handler on 10.01.20.
//  Copyright © 2020 Nino Handler. All rights reserved.
//

import Foundation
import SharedCode

class AstronautsViewModel : ObservableObject {
    @Published var astronauts: [Astronaut] = []
    
    init() {
        Repository.init().fetchPeople(success: { astronauts in
            self.astronauts = astronauts
        })
    }
}
