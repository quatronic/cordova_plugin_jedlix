package com.quatronic.jedlixplugin

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedlix.sdk.JedlixSDK
import com.jedlix.sdk.connectSession.ConnectSessionResult
import com.jedlix.sdk.connectSession.registerConnectSessionManager
import com.jedlix.sdk.connectSession.ConnectSessionType
import java.net.URL

class ConnectionActivity : AppCompatActivity() {
    private lateinit var userIdentifier: String
    private lateinit var vehicleIdentifier: String
    private lateinit var accessToken: String

    companion object {
        lateinit var baseURL: URL
        lateinit var apiKey: String
        lateinit var authentication: DefaultAuthentication
    }
     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseURL = URL("https://qa-nextenergy-smartcharging.jedlix.com")

        userIdentifier = intent.getStringExtra("userId") ?: ""
        vehicleIdentifier = intent.getStringExtra("vehicleId") ?: ""
        accessToken = intent.getStringExtra("accessToken") ?: ""
        apiKey = intent.getStringExtra("apiKey") ?: ""
        
        try {
            authentication = DefaultAuthentication(this)
            JedlixSDK.configure(baseURL, apiKey, authentication)
            authentication.setCredentials(accessToken, userIdentifier)
            
        } catch (e: Exception) {
            println("Authentication error")
        }

        val connectSessionManager = registerConnectSessionManager { result ->
            when (result) {
                is ConnectSessionResult.Finished -> {
                    val intent = Intent().also {
                        it.putExtra("sessionId", result.sessionId)
                    }
                    setResult(Activity.RESULT_OK, intent)

                    finish()
                }
                is ConnectSessionResult.InProgress -> {println("In progress")}
                is ConnectSessionResult.NotStarted -> {println("Not started")}
            }

        }
        
        connectSessionManager.startConnectSession(
            userIdentifier,
            ConnectSessionType.SelectedVehicle(vehicleIdentifier)
        )

    }
}