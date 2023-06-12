package com.quatronic.jedlixplugin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedlix.sdk.connectSession.registerConnectSessionManager
import com.jedlix.sdk.connectSession.ConnectSessionType

class ConnectionsActivity : AppCompatActivity() {
    private lateinit var userIdentifier: String
    private lateinit var vehicleIdentifier: String
     
    override fun onCreate(savedInstanceState: Bundle?) {
        userIdentifier = intent.getStringExtra("userId") ?: ""
        vehicleIdentifier = intent.getStringExtra("vehicleId") ?: ""
        
        val connectSessionManager = registerConnectSessionManager { result ->
            //To do
        }
        
        connectSessionManager.startConnectSession(
            userIdentifier,
            ConnectSessionType.SelectedVehicle(vehicleIdentifier)
        )
    }
}