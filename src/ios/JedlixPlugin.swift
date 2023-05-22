import SwiftUI
import JedlixSDK

class JedlixPlugin: CDVPlugin {
    @objc(coolMethod:)
    func coolMethod(command: CDVInvokedUrlCommand) {

        let options = command.arguments[0];

        let vc = ConnectView.create(userId: options.userid, accessToken: options.accesstoken, vehicleId: options.vehicleid)
        UIApplication.shared.keyWindow?.rootViewController?.present(vc, animated: true, completion: nil)

        let result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "Test")
        self.commandDelegate.send(result, callbackId: command.callbackId)
    }
}


@objc class ConnectView: NSObject {
     @objc static func create(userId: String, accessToken: String, vehicleId: String) -> UIViewController {
         let baseURL = URL(string: "https://demo-smartcharging.jedlix.com")!
         let apiKey: String? = nil
         //let accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5VTTJPRGxFT0VSQlJqVkNPRFEwTVRVeVJrTkdRMFF4UTBWRk9EQXpRek0wUWprelF6SkZRZyJ9.eyJodHRwOi8vc2NoZW1hLmplZGxpeC5jb20vcm9sZXMiOlsidXNlciJdLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJmcmFuay5ncm9vdGVuQHF1YXRyb25pYy5ubCIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL25pY2tuYW1lIjoiZnJhbmsuZ3Jvb3RlbiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL3V1aWQiOiIwOGNjMTdjOC05NTYwLTRiZDMtYTZmMi0wZTYxODI0ZTQwYTIiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9hY2NvdW50aWQiOiI4ZjA5NWU0ZC01ZmZkLTQwNTktOGI0Yi0zMWU5ZWNlYzcyMmIiLCJpc3MiOiJodHRwczovL2plZGxpeC1iMmUuZXUuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDY0NDc5N2VkOGNiZDJhZGM0ODc4MTc0YSIsImF1ZCI6WyJodHRwczovL2RldmVsb3Blci1wb3J0YWwuamVkbGl4LmNvbSIsImh0dHBzOi8vamVkbGl4LWIyZS5ldS5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNjg0MjMzNTU5LCJleHAiOjE2ODU0NDMxNTksImF6cCI6ImNqMnJEbjFqdjVoMkhjUHRTYWZzeDl2YUJYbDd2Q0FqIiwic2NvcGUiOiJvcGVuaWQgYXBwIG9mZmxpbmVfYWNjZXNzIn0.DiaQ4Q0B57fmJHmW43V24oMsE70ZJSh44WWyBGP0VHikp87fuDqqwAzJvDq69_-eICXu3tnShrAspz3Q5VUPq32gn6Zwo8zBvDG-JExGwIvigGWQraqBsG-BU6yco6kODEGOn1Tzi3ejEsa-iUK8FCj9gLlsotADCpus3sKhJyhOHxa42pi9WETozvvVYe5wnBEqb7cS_NsuAy3jPa6sPFgpUOAHG9D3hrTcqmNCAJ_EhhydFd9WaG5sq0R1XT8_8W-Yf2KOfnJniHci4p0rVxElX3DLC2XFywybWSooD90icK8xmVWY7KJSchTdxOfuiGu1MKl6ve2JzYWB0i9nww"
         //let userId = "08cc17c8-9560-4bd3-a6f2-0e61824e40a2"
        
         let authentication = DefaultAuthentication()
         JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
         authentication.authenticate(accessToken: accessToken, userIdentifier: userId)
        
         return UIHostingController(rootView: ConnectSessionView(userIdentifier: userId, sessionType: .vehicle))
     }
}
