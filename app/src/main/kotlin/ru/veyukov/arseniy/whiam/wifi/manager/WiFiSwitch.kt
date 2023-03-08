
package ru.veyukov.arseniy.whiam.wifi.manager

import android.annotation.TargetApi
import android.net.wifi.WifiManager
import android.os.Build
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.buildMinVersionQ
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.startWiFiSettings

@OpenClass
class WiFiSwitch(private val wifiManager: WifiManager) {
    fun on(): Boolean = enable(true)

    fun off(): Boolean = enable(false)

    fun startWiFiSettings(): Unit = MainContext.INSTANCE.mainActivity.startWiFiSettings()

    fun minVersionQ(): Boolean = buildMinVersionQ()

    private fun enable(enabled: Boolean): Boolean = if (minVersionQ()) enableWiFiAndroidQ() else enableWiFiLegacy(enabled)

    @TargetApi(Build.VERSION_CODES.Q)
    private fun enableWiFiAndroidQ(): Boolean {
        startWiFiSettings()
        return true
    }

    @Suppress("DEPRECATION")
    private fun enableWiFiLegacy(enabled: Boolean): Boolean = wifiManager.setWifiEnabled(enabled)

}