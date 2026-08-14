import SwiftUI
import Shared

@main
struct iOSApp: App {
    // todo check
    init() {
        KoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
