package com.quatronic.jedlixplugin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.net.URL
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.apache.cordova.PluginResult
import com.jedlix.sdk.JedlixSDK


class JedlixPlugin : CordovaPlugin() {
    companion object {
        lateinit var baseURL: URL
        lateinit var apiKey: String
        lateinit var authentication: Authentication
    }

    var callbackContext: CallbackContext? = null

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        this.callbackContext = callbackContext

        if (action == "coolMethod") {
            val apiKey = args.optString(0, "")
            val userId = args.optString(1, "")
            val accessToken = args.optString(2, "")
            val vehicleId = args.optString(3, "")

            var result: PluginResult

            baseURL = URL("https://qa-nextenergy-smartcharging.jedlix.com")

            try {
                authentication = DefaultAuthentication(cordova.getActivity())
                authentication?.also { it .setCredential(accessToken, userId) }
                JedlixSDK.configure(baseURL, apiKey, authentication)
            } catch (e: Exception) {
                result = PluginResult(PluginResult.Status.ERROR, "Authentication error " + e.message)
                callbackContext.sendPluginResult(result)
            }
            
            try {
                val intent = Intent(cordova.getContext(), ConnectionActivity::class.java)
                intent.putExtra("userId", userId)
                intent.putExtra("vehicleId", vehicleId)

                cordova.setActivityResultCallback(this);
                cordova.getActivity().startActivityForResult(intent, 100)
            } catch (e: Exception) {
                result = PluginResult(PluginResult.Status.ERROR, "Error starting the activity: " + e.message)
                callbackContext.sendPluginResult(result)
            }
            
            //Standard Cordova stuff
            result = PluginResult(PluginResult.Status.NO_RESULT)
            result.setKeepCallback(true)
            callbackContext.sendPluginResult(result)
            return true
        }
        
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        val result = PluginResult(PluginResult.Status.OK, data.getStringExtra("sessionId")?:"")
        callbackContext?.sendPluginResult(result)
    }
}

