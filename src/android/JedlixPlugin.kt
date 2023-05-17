import com.jedlix.sdk.JedlixSDK
import org.apache.cordova.CordovaPlugin

class JedlixPlugin : CordovaPlugin() {

    companion object {
        lateinit var baseURL: URL
        lateinit var apiKey: String
        lateinit var authentication: Authentication
    }

    override fun onCreate() {
        super.onCreate()

        baseURL = URL("<YOUR BASE URL>")
        apiKey = "<YOUR API KEY>"
        authentication = DefaultAuthentication(this)

        JedlixSDK.configure(
            baseURL,
            apiKey,
            authentication
        )
    }

}

class SomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val connectSessionManager = registerConnectSessionManager { result ->
            // continue when ConnectSessionActivity finishes
        }

        findViewById<View>(R.id.some_button).setOnClickListener {
            connectSessionManager.startConnectSession(
                "<USER ID>",
                ConnectSessionType.Vehicle
            )
        }
    }
}