import SwiftUI
import JedlixSDK

class JedlixPlugin: CDVPlugin {
    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {
        let userid = command.arguments[0] as? String ?? ""
        let accesstoken = command.arguments[1] as? String ?? ""
        let vehicleid = command.arguments[2] as? String ?? ""
        
        let vc = ConnectView.createVehicle(userId: userid, accessToken: accesstoken, vehicleId: vehicleid) { result in
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAsMultipart: result)
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
        }

        UIApplication.shared.rootViewController?.present(vc, animated: true, completion: nil)

        let pluginResult = CDVPluginResult(status: CDVCommandStatus_NO_RESULT)
        pluginResult.setKeepCallback(true)
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(chargerMethod:)
    func chargerMethod(command: CDVInvokedUrlCommand) {
        let userid = command.arguments[0] as? String ?? ""
        let accesstoken = command.arguments[1] as? String ?? ""
        let chargingLocationid = command.arguments[2] as? String ?? ""
        
        let vc = ConnectView.createCharger(userId: userid, accessToken: accesstoken, chargingLocationId: chargingLocationid)
        UIApplication.shared.rootViewController?.present(vc, animated: true, completion: nil)
        
        let resultcharger = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Test")
        self.commandDelegate.send(resultcharger, callbackId: command.callbackId)
    }
}


@objc class ConnectView: NSObject {
    @objc static func createVehicle(userId: String, accessToken: String, vehicleId: String, callback: @escaping ([String]) -> Void) -> UIViewController {
        let baseURL = URL(string: "https://qa-nextenergy-smartcharging.jedlix.com")!
        let apiKey: String? = nil
        
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
    
    @objc static func createCharger(userId: String, accessToken: String, chargingLocationId: String) -> UIViewController {
        let baseURL = URL(string: "https://qa-nextenergy-smartcharging.jedlix.com")!
        let apiKey: String? = nil
        let authentication = DefaultAuthentication()
        let type = ConnectSessionType.charger(chargingLocationId: chargingLocationId)
        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)
        return UIHostingController(rootView: ConnectSessionView(userIdentifier: userId, sessionType: type))
    }
}

extension UIApplication {
    var rootViewController: UIViewController? {
        UIApplication.shared.windows.filter { $0.isKeyWindow }.first?.rootViewController
    }
}