package com.quatronic.jedlixplugin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.apache.cordova.PluginResult



class JedlixPlugin : CordovaPlugin() {
    var callbackContext: CallbackContext? = null

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        this.callbackContext = callbackContext

        if (action == "coolMethod") {
            val apiKey = args.optString(0, "")
            val userId = args.optString(1, "")
            val accessToken = args.optString(2, "")
            val vehicleId = args.optString(3, "")

            var result: PluginResult
      
            try {
                val intent = Intent(cordova.getContext(), ConnectionActivity::class.java)
                intent.putExtra("userId", userId)
                intent.putExtra("vehicleId", vehicleId)
                intent.putExtra("accessToken", accessToken)
                intent.putExtra("apiKey", apiKey)

                //cordova.setActivityResultCallback(this);
                this.cordova.startActivityForResult(this, intent, 100)
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

