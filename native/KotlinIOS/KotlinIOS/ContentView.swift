//
//  ContentView.swift
//  KotlinIOS
//
//  Created by Nino Handler on 08.01.20.
//  Copyright © 2020 Nino Handler. All rights reserved.
//

import SwiftUI
import SharedCode

struct ContentView: View {
    let message = CommonKt.createApplicationScreenMessage()
    var body: some View {
        Text(message)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
