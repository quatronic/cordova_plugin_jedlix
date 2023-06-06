import SwiftUI
import JedlixSDK

class JedlixPlugin: CDVPlugin {
    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {

        let userid = command.arguments[0] as? String ?? ""
        let vehicleid = command.arguments[1] as? String ?? ""

        let vc = ConnectView.create(userId: userid, vehicleId: vehicleid)
        UIApplication.shared.keyWindow?.rootViewController?.present(vc, animated: true, completion: nil)

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Test")
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }
}

//class JedlixPlugin: CVDPlugin { 
//    @objc(chargerMethod:)
//    func chargerMethod(command: CDVInvokedUrlCommand) {
        
//        let userid = command.arguments[0] as? String ?? ""
//        let accesstoken = command.arguments[1] as? String ?? ""
//        let charginglocationid = command.arguments[2] as? String ?? ""
//        let externalid = command.arguments[3] as? String ?? ""
        
        //charger specific code similar to the one in the previous class
//    }
//}

//@objc class ChargerConnectView: NSObject {
 //   @objc static func create(userid: String, accessToken: String, chargerid: String, charginglocationid: String)
    
//}
//Define a class similar to the one below but for a charger connection instead

struct OSAuthentication: Decodable {
    let accesstoken: String
    let issuccess: Bool
}

func GetToken() async throws -> OSAuthentication {
    let OSid = "e749f09e-491d-4ac2-9d4e-16f9ef700bbc"
    let url = URL(string: "https://energynextbv-dev.outsystemsenterprise.com/Jedlix_IS/rest/GetAccessToken/GetToken?AuthenticationId=\(OSid)")!
    
    let (data, _) = try await URLSession.shared.data(from: url)
    
    let decoded = try JSONDecoder().decode(OSAuthentication.self, from: data)
    
    return decoded
}

@objc class ConnectView: NSObject {
    @objc static func create(userId: String, vehicleId: String) -> UIViewController {
        let baseURL = URL(string: "https://qa-nextenergy-smartcharging.jedlix.com")!
        let apiKey: String? = nil
        
        Task {
            do {
                let accessToken = try await GetToken()
                
                let authentication = DefaultAuthentication()
                JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
                authentication.authenticate(accessToken: accessToken.accesstoken, userIdentifier: userId)
            } catch {
                print("Error fetching access token: \(error)")
            }
        }
        
        return UIHostingController(rootView: ConnectSessionView(userIdentifier: userId, vehicleIdentifier: vehicleId))
    }
}

