package com.quatronic.jedlixplugin

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jedlix.sdk.connectSession.ConnectSessionResult
import com.jedlix.sdk.connectSession.registerConnectSessionManager
import com.jedlix.sdk.connectSession.ConnectSessionType

class ConnectChargerActivity : AppCompatActivity() {
    private lateinit var userIdentifier: String
    private lateinit var chargingLocationId: String
    private lateinit var openSessionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userIdentifier = intent.getStringExtra("userId") ?: ""
        chargingLocationId = intent.getStringExtra("chargingLocationId") ?: ""
        sessionIdentifier = intent.getStringExtra("openSessionId") ?: ""

        val connectSessionManager = registerConnectSessionManager { result ->
            when (result) {
                
                is ConnectSessionResult.Finished -> {
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)

                    finish()
                }
                is ConnectSessionResult.InProgress -> {println("In progress")}
                is ConnectSessionResult.NotStarted -> {println("Not started")}
                
            }

        }
        
        if(openSessionId=="") {
            connectSessionManager.startConnectSession(
                userIdentifier,
                ConnectSessionType.Charger(chargingLocationId)
            )
        }
        else {
            connectSessionManager.startConnectSession(
                userIdentifier,
                sessionIdentifier
            )
        }

    }
}