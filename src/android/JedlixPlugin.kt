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
        lateinit var authentication: DefaultAuthentication
    }

    var callbackContext: CallbackContext? = null

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        this.callbackContext = callbackContext

        if (action == "coolMethod") {
            val apiKey = args.optString(0, "")
            val userId = args.optString(1, "")
            val accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5VTTJPRGxFT0VSQlJqVkNPRFEwTVRVeVJrTkdRMFF4UTBWRk9EQXpRek0wUWprelF6SkZRZyJ9.eyJpc3MiOiJodHRwczovL2plZGxpeC1iMmUuZXUuYXV0aDAuY29tLyIsInN1YiI6IjlQTjlpRzhFMDZvZDZtZzlRRTRZcHEwbTBBeDZDTFJsQGNsaWVudHMiLCJhdWQiOiJodHRwczovL3FhLXNtYXJ0Y2hhcmdpbmcuamVkbGl4LmNvbSIsImlhdCI6MTY5MDE5OTE3OSwiZXhwIjoxNjkxNDA4Nzc5LCJhenAiOiI5UE45aUc4RTA2b2Q2bWc5UUU0WXBxMG0wQXg2Q0xSbCIsInNjb3BlIjoicmVhZDphbGwtc2Vzc2lvbnMgdXNlci1yZXNvdXJjZXMgcmVhZDphbGwtY2hhcmdpbmdsb2NhdGlvbnMgY3JlYXRlOnVzZXIgcmVhZDphbGwtdmVoaWNsZXMgcmVhZDphbGwtdXNlcnMgdGVuYW50Om5leHRlbmVyZ3kiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.he-8e0tc9tMxH6k-oDnknVdJm_QrvrHnvvGjLJvhrrgG4nHt9fd3fxJ92fO1OzLbojIaiWSgxvgbskhbj8CyJ5WBl8Hcce5Zxzn3FuI2LQ11HPbpxSCPg7Mr4VHM8i7t_SbtDPuY-mHAvz5BRDMpHwlGSsKrwC-L4KhZ-sr9dyGgHh-O8x_k2l_adSAGIx1QT9Bx4fzfS7ZaYjWaJoYxscSXqC1a0ch-17Lij5Mmnsv8za_pFGHOhRbQLZT6uh02gTJxfpSIHqL3uKf4hNDgiqU8xfOextJuj2pN0N0IPjXNAAJSl8Rd-omWbTJiMQEpoYJ_CSs9eEdYJXM_dyE8mA"
            val vehicleId = args.optString(3, "")

            var result: PluginResult

            baseURL = URL("https://qa-nextenergy-smartcharging.jedlix.com")

            try {
                authentication = DefaultAuthentication(cordova.getActivity())
                JedlixSDK.configure(baseURL, apiKey, authentication)
                authentication.setCredentials(accessToken, userId)
                
            } catch (e: Exception) {
                result = PluginResult(PluginResult.Status.ERROR, "Authentication error " + e.message)
                callbackContext.sendPluginResult(result)
            }
            
            try {
                val intent = Intent(cordova.getContext(), ConnectSessionActivity::class.java)
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

