
package ru.veyukov.arseniy.whiam

import android.content.Context
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import ru.veyukov.arseniy.whiam.scheme.Scheme
import ru.veyukov.arseniy.whiam.settings.Repository
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService
import ru.veyukov.arseniy.whiam.wifi.scanner.makeScannerService

enum class MainContext {
    INSTANCE;

    lateinit var settings: Settings
    lateinit var mainActivity: MainActivity
    lateinit var wiFiManagerWrapper: WiFiManagerWrapper
    lateinit var scannerService: ScannerService
    lateinit var configuration: Configuration
    lateinit var scheme: Scheme

    val context: Context
        get() = mainActivity.applicationContext

    val resources: Resources
        get() = context.resources

    val layoutInflater: LayoutInflater
        get() = mainActivity.layoutInflater

    private val wiFiManager: WifiManager
        get() = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun initialize(activity: MainActivity, largeScreen: Boolean) {
        mainActivity = activity
        configuration = Configuration(largeScreen)
        settings = Settings(Repository(context))
        wiFiManagerWrapper = WiFiManagerWrapper(wiFiManager)
        scannerService = makeScannerService(mainActivity, wiFiManagerWrapper, Handler(Looper.getMainLooper()), settings)
        scheme=Scheme(activity.resources)
    }

}