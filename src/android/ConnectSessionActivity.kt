package com.quatronic.jedlixplugin

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedlix.sdk.connectSession.ConnectSessionResult
import com.jedlix.sdk.connectSession.registerConnectSessionManager
import com.jedlix.sdk.connectSession.ConnectSessionType

class ConnectSessionActivity : AppCompatActivity() {
    private lateinit var userIdentifier: String
    private lateinit var vehicleIdentifier: String
     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userIdentifier = intent.getStringExtra("userId") ?: ""
        vehicleIdentifier = intent.getStringExtra("vehicleId") ?: ""

        val connectSessionManager = registerConnectSessionManager { result ->
            when (result) {
                /*
                is ConnectSessionResult.Finished -> {
                    val intent = Intent().also {
                        it.putExtra("sessionId", result.sessionId)
                    }
                    setResult(Activity.RESULT_OK, intent)

                    finish()
                }
                is ConnectSessionResult.InProgress -> {println("In progress")}
                is ConnectSessionResult.NotStarted -> {println("Not started")}
                */
            }

        }
        
        connectSessionManager.startConnectSession(
            userIdentifier,
            ConnectSessionType.SelectedVehicle(vehicleIdentifier)
        )

    }
}