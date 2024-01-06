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
       let mainViewController = MainViewControllerKt.MainViewController()
       // Set the background color to clear (transparent) in SwiftUI style
       let uiViewController = UIHostingController(rootView: Color.clear)
       mainViewController.addChild(uiViewController)
       mainViewController.view.addSubview(uiViewController.view)
       uiViewController.didMove(toParent: mainViewController)
       return mainViewController
       //MainViewControllerKt.MainViewController()

    }

}
