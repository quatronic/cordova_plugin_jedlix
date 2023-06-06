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

func GetToken(completion: @escaping (OSAuthentication?, Error?) -> Void) {
    let OSid = "e749f09e-491d-4ac2-9d4e-16f9ef700bbc"
    let url = URL(string: "https://energynextbv-dev.outsystemsenterprise.com/Jedlix_IS/rest/GetAccessToken/GetToken?AuthenticationId=\(OSid)")!
    
    
URLSession.shared.dataTask(with: url) { data, _, error in
    if let error = error {
        // Handle the error case
        completion(nil, error)
        return
    }
    
    guard let data = data else {
        // Handle the case where no data is received
        let error = NSError(domain: "YourDomain", code: 0, userInfo: [NSLocalizedDescriptionKey: "No data received"])
        completion(nil, error)
        return
    }
    
    // Process the received data
    do {
        let decoded = try JSONDecoder().decode(OSAuthentication.self, from: data)
        completion(decoded, nil)
    } catch {
        // Handle the case where decoding fails
        completion(nil, error)
    }
}.resume()
}



@objc class ConnectView: NSObject {
    @objc static func create(userId: String, vehicleId: String) -> UIViewController {
        let baseURL = URL(string: "https://demo-smartcharging.jedlix.com")!
        let apiKey: String? = nil
        let accessToken = try? await GetToken() // Await the GetToken function call
        
        let authentication = DefaultAuthentication()
        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        
        if let accessToken = accessToken {
            authentication.authenticate(accessToken: accessToken.accesstoken, userIdentifier: userId)
        }
        
        return UIHostingController(rootView: ConnectSessionView(userIdentifier: userId, vehicleIdentifier: vehicleId))
    }
}
