import SwiftUI
import JedlixSDK

@objc(JedlixPlugin) class JedlixPlugin: CDVPlugin {
    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {
        let url = command.arguments[0] as? String ?? ""
        let apiKey = command.arguments[1] as? String ?? ""
        let userid = command.arguments[2] as? String ?? ""
        let accesstoken = command.arguments[3] as? String ?? ""
        let vehicleid = command.arguments[4] as? String ?? ""
        let sessionid = command.arguments[5] as? String ?? ""
        
        let vc = ConnectView.createVehicle(url: url, apiKey: apiKey, userId: userid, accessToken: accesstoken, vehicleId: vehicleid, openSessionId: sessionid) { result in
            let pluginResult = result[0] == "finished" ? CDVPluginResult(status: CDVCommandStatus_OK, messageAsMultipart: result) : CDVPluginResult(status: CDVCommandStatus_ERROR, messageAsMultipart: result)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
        
        UIApplication.shared.rootViewController?.present(vc, animated: true, completion: nil)
    }
    
    @objc(chargerMethod:)
    func chargerMethod(command: CDVInvokedUrlCommand) {
        let url = command.arguments[0] as? String ?? ""
        let apiKey = command.arguments[1] as? String ?? ""
        let userid = command.arguments[2] as? String ?? ""
        let accesstoken = command.arguments[3] as? String ?? ""
        let chargingLocationid = command.arguments[4] as? String ?? ""
        let sessionid = command.arguments[5] as? String ?? ""
        
        let vc = ConnectView.createCharger(url: url, apiKey: apiKey, userId: userid, accessToken: accesstoken, chargingLocationId: chargingLocationid, openSessionId: sessionid) { result in
            let pluginResult = result[0] == "finished" ? CDVPluginResult(status: CDVCommandStatus_OK, messageAsMultipart: result) : CDVPluginResult(status: CDVCommandStatus_ERROR, messageAsMultipart: result)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }

        UIApplication.shared.rootViewController?.present(vc, animated: true, completion: nil)
    }
}


@objc class ConnectView: NSObject {
    @objc static func createVehicle(url: String, apiKey: String, userId: String, accessToken: String, vehicleId: String, openSessionId: String, callback: @escaping ([String]) -> Void) -> UIViewController {
        let baseURL = URL(string: url)!     
        let authentication = DefaultAuthentication()

        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)
        
        if (openSessionId == "") {
            return UIHostingController(rootView:
                ConnectSessionView(userIdentifier: userId, vehicleIdentifier: vehicleId) { result in
                    switch result {
                    case .notStarted: callback(["notStarted"])
                    case .inProgress(let sessionId): callback(["inProgress", sessionId])
                    case .finished(let sessionId): callback(["finished", sessionId])
                    default: break
                    }
                }
            )
        }
        else {
            return UIHostingController(rootView:
                ConnectSessionView(userIdentifier: userId, sessionIdentifier: openSessionId) { result in
                    switch result {
                    case .notStarted: callback(["notStarted"])
                    case .inProgress(let sessionId): callback(["inProgress", sessionId])
                    case .finished(let sessionId): callback(["finished", sessionId])
                    default: break
                    }
                }
            )
        }      
    }
    
    @objc static func createCharger(url: String, apiKey: String, userId: String, accessToken: String, chargingLocationId: String, openSessionId: String, callback: @escaping ([String]) -> Void) -> UIViewController {
        let baseURL = URL(string: url)!
        let authentication = DefaultAuthentication()
        let type = ConnectSessionType.charger(chargingLocationId: chargingLocationId)

        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)

        if (openSessionId == "") {
            return UIHostingController(rootView: 
                ConnectSessionView(userIdentifier: userId, sessionType: type) { result in
                    switch result {
                    case .notStarted: callback(["notStarted"])
                    case .inProgress(let sessionId): callback(["inProgress", sessionId])
                    case .finished(let sessionId): callback(["finished", sessionId])
                    default: break
                    }
                }
            )
        }
        else {
            return UIHostingController(rootView: 
                ConnectSessionView(userIdentifier: userId, sessionIdentifier: openSessionId) { result in
                    switch result {
                    case .notStarted: callback(["notStarted"])
                    case .inProgress(let sessionId): callback(["inProgress", sessionId])
                    case .finished(let sessionId): callback(["finished", sessionId])
                    default: break
                    }
                }
            )
        }
    }
}

extension UIApplication {
    var rootViewController: UIViewController? {
        UIApplication.shared.windows.filter { $0.isKeyWindow }.first?.rootViewController
    }
}