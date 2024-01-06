import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        HelperKt.doInitKoin()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
			.onAppear
			    {
			        if let window = UIApplication.shared.windows.first
			            {
                           window.backgroundColor = UIColor.clear
                        }
                }
		}
	}
}
