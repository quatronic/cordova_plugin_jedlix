import SwiftUI
import JedlixSDK

class JedlixPlugin: CDVPlugin {
    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {
        let userid = command.arguments[0] as? String ?? ""
        let accesstoken = command.arguments[1] as? String ?? ""
        let vehicleid = command.arguments[2] as? String ?? ""
        
        let vc = ConnectView.createVehicle(userId: userid, accessToken: accesstoken, vehicleId: vehicleid)
        UIApplication.shared.keyWindow?.rootViewController?.present(vc, animated: true, completion: nil)

        let resultvehicle = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Test")
        self.commandDelegate.send(resultvehicle, callbackId: command.callbackId)
    }
    
    @objc(chargerMethod:)
    func chargerMethod(command: CDVInvokedUrlCommand) {
        let userid = command.arguments[0] as? String ?? ""
        let accesstoken = command.arguments[1] as? String ?? ""
        let chargingLocationid = command.arguments[2] as? String ?? ""
        
        let cc = ConnectView.createCharger(userId: userid, accessToken: accesstoken, chargingLocationId: chargingLocationid)
        UIApplication.shared.keyWindow?.rootViewController?.present(cc, animated: true, completion: nil)
        
        let resultcharger = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Test")
        self.commandDelegate.send(resultcharger, callbackId: command.callbackId)
    }
}


@objc class ConnectView: NSObject {
    @objc static func createVehicle(userId: String, accessToken: String, vehicleId: String) -> UIViewController {
        let baseURL = URL(string: "https://qa-nextenergy-smartcharging.jedlix.com")!
        let apiKey: String? = nil
        
        let authentication = DefaultAuthentication()
        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)
        
        return UIHostingController(rootView: ConnectSessionView(userIdentifier: userId, vehicleIdentifier: vehicleId))
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