//
//  ConnectView.swift
//  HelloCordova
//
//  Created by Tom Stanik on 08/05/2023.
//

import SwiftUI
import JedlixSDK

@objc class ConnectView: NSObject {
    @objc static func connect() -> UIViewController {
        let baseURL = URL(string: "")!
        let apiKey = ""
        let accessToken = ""
        let userId = ""
        
        let authentication = DefaultAuthentication()
        JedlixSDK.configure(baseURL: baseURL, apiKey: apiKey, authentication: authentication)
        authentication.authenticate(accessToken: accessToken, userIdentifier: userId)
        
        return UIHostingController(rootView: ConnectSessionView(userIdentifier: userId, sessionType: .vehicle))
    }
}
