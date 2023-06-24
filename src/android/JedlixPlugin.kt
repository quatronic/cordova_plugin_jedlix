package com.quatronic.jedlixplugin

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.apache.cordova.PluginResult
import com.example.jedlixsdk.ConnectView
import com.example.jedlixsdk.R
import com.example.jedlixsdk.ui.ConnectSessionActivity

class JedlixPlugin : CordovaPlugin() {

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        if (action == "coolMethod") {
            val userId = args.optString(0, "")
            val accessToken = args.optString(1, "")
            val vehicleId = args.optString(2, "")
                                 
            val intent = ConnectView.create(userId, accessToken, vehicleId)
            
            startActivity(intent)
            
            val result = PluginResult(PluginResult.Status.OK, "Test")
            callbackContext.sendPluginResult(result)
            
            return true
        }
        
        return false
    }
}

class ConnectView {

    companion object {
        @JvmStatic
        fun create(userId: String, accessToken: String, vehicleId: String): Intent {
            val baseURL = "https://qa-nextenergy-smartcharging.jedlix.com"
            val apiKey: String? = null
            // val accessToken = "your_access_token"
            // val userId = "your_user_id"
            
            val authentication = DefaultAuthentication()
            JedlixSDK.configure(baseURL, apiKey, authentication)
            authentication.authenticate(accessToken, userId)
            
            val intent = Intent(this, SomeActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("vehicleId", vehicleId)
            
            return intent
        }
    }
}

class SomeActivity : AppCompatActivity() {
    private lateinit var userIdentifier: String
    private lateinit var vehicleIdentifier: String
     
    override fun onCreate(savedInstanceState: Bundle?) {
        userIdentifier = intent.getStringExtra("userId") ?: ""
        vehicleIdentifier = intent.getStringExtra("vehicleId") ?: ""
        
        val connectSessionManager = registerConnectSessionManager { result ->
            // continue when ConnectSessionActivity finishes
        }
        
        connectSessionManager.startConnectSession(
            userIdentifier,
            ConnectSessionType.SelectedVehicle(vehicleIdentifier)
        )
    }
}
