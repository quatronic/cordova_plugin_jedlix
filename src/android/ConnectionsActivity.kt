package com.quatronic.jedlixplugin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedlix.sdk.connectSession.registerConnectSessionManager
import com.jedlix.sdk.connectSession.ConnectSessionType

class ConnectionActivity : AppCompatActivity() {
    private lateinit var userIdentifier: String
    private lateinit var vehicleIdentifier: String
     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userIdentifier = intent.getStringExtra("userId") ?: ""
        vehicleIdentifier = intent.getStringExtra("vehicleId") ?: ""
        println("--- User " + userIdentifier + " vehicle " + vehicleIdentifier)
        val connectSessionManager = registerConnectSessionManager { result ->
            // Activity.RESULT_OK Activity.RESULT_CANCELED result.sessionId/ 
        }
        
        connectSessionManager.startConnectSession(
            userIdentifier,
            ConnectSessionType.SelectedVehicle(vehicleIdentifier)
        )


    }

    fun onResume


}