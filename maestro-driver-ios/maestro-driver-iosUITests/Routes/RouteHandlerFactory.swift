import Foundation

class RouteHandlerFactory {
    class func createRouteHandler(route: Route) -> RouteHandler {
        switch route {
        case .subTree:
            return SubTreeRouteHandler()
        case .runningApp:
            return RunningAppRouteHandler()
        case .swipe:
            return SwipeRouteHandler()
        case .inputText:
            return InputTextRouteHandler()
        case .touch:
            return TouchRouteHandler()
        case .screenshot:
            return ScreenshotHandler()
        }
    }
}
