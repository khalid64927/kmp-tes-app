//
//  ComposeView.swifts
//  iosContactsMP
//
//  Created by Rodrigo Guerrero on 13/07/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct ComposeView: UIViewControllerRepresentable {

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        let viewController = MainViewControllerKt.TransparentMainViewController()
        return viewController
    }

}
