import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.jedlixsdk.ConnectView
import com.example.jedlixsdk.R
import com.example.jedlixsdk.ui.ConnectSessionActivity

class JedlixPlugin : CordovaPlugin() {

    override fun execute(action: String, args: JSONArray, callbackContext: CallbackContext): Boolean {
        if (action == "coolMethod") {
            val userId = args.optString(0, "")
            val accessToken = args.optString(1, "")
            val vehicleId = args.optString(2, "")
            
            val intent = Intent(cordova.activity, ConnectSessionActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("accessToken", accessToken)
            intent.putExtra("vehicleId", vehicleId)
            
            cordova.activity?.startActivity(intent)
            
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
            val baseURL = "https://demo-smartcharging.jedlix.com"
            val apiKey: String? = null
            // val accessToken = "your_access_token"
            // val userId = "your_user_id"
            
            val authentication = DefaultAuthentication()
            JedlixSDK.configure(baseURL, apiKey, authentication)
            authentication.authenticate(accessToken, userId)
            
            val intent = Intent(this, ConnectSessionActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("vehicleId", vehicleId)
            
            return intent
        }
    }
}

class ConnectSessionActivity : AppCompatActivity() {

    private lateinit var userIdentifier: String
    private lateinit var vehicleIdentifier: String
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_session)
        
        userIdentifier = intent.getStringExtra("userId") ?: ""
        vehicleIdentifier = intent.getStringExtra("vehicleId") ?: ""
        
        val rootView = ConnectSessionView(userIdentifier, vehicleIdentifier)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, rootView)
            .commit()
    }
}
