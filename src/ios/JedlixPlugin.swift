import SwiftUI
import JedlixSDK

@objc(JedlixPlugin) class JedlixPlugin: CDVPlugin {
    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {
        let apiKey = command.arguments[0] as? String ?? ""
        let userid = command.arguments[1] as? String ?? ""
        let accesstoken = command.arguments[2] as? String ?? ""
        let vehicleid = command.arguments[3] as? String ?? ""
        
        let vc = ConnectView.createVehicle(apiKey: apiKey, userId: userid, accessToken: accesstoken, vehicleId: vehicleid) { result in
            let pluginResult = result[0] == "finished" ? CDVPluginResult(status: CDVCommandStatus_OK) : CDVPluginResult(status: CDVCommandStatus_ERROR, messageAsMultipart: result)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }
        
        UIApplication.shared.rootViewController?.present(vc, animated: true, completion: nil)
    }
    
    @objc(chargerMethod:)
    func chargerMethod(command: CDVInvokedUrlCommand) {
        let apiKey = command.arguments[0] as? String ?? ""
        let userid = command.arguments[1] as? String ?? ""
        let accesstoken = command.arguments[2] as? String ?? ""
        let chargingLocationid = command.arguments[3] as? String ?? ""
        
        let vc = ConnectView.createCharger(apiKey: apiKey, userId: userid, accessToken: accesstoken, chargingLocationId: chargingLocationid) { result in
            let pluginResult = result[0] == "finished" ? CDVPluginResult(status: CDVCommandStatus_OK) : CDVPluginResult(status: CDVCommandStatus_ERROR, messageAsMultipart: result)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }

        UIApplication.shared.rootViewController?.present(vc, animated: true, completion: nil)
    }
}


@objc class ConnectView: NSObject {
    @objc static func createVehicle(apiKey: String, userId: String, accessToken: String, vehicleId: String, callback: @escaping ([String]) -> Void) -> UIViewController {
        let baseURL = URL(string: "https://qa-nextenergy-smartcharging.jedlix.com")!     
        let authentication = DefaultAuthentication()

        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)
        
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
    
    @objc static func createCharger(apiKey: String, userId: String, accessToken: String, chargingLocationId: String) -> UIViewController {
        let baseURL = URL(string: "https://qa-nextenergy-smartcharging.jedlix.com")!
        let authentication = DefaultAuthentication()
        let type = ConnectSessionType.charger(chargingLocationId: chargingLocationId)

        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)

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
}

extension UIApplication {
    var rootViewController: UIViewController? {
        UIApplication.shared.windows.filter { $0.isKeyWindow }.first?.rootViewController
    }
}