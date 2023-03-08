
package ru.veyukov.arseniy.whiam.wifi.manager

import android.annotation.SuppressLint
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.buildMinVersionR

@OpenClass
class WiFiManagerWrapper(
    private val wifiManager: WifiManager,
    private val wiFiSwitch: WiFiSwitch = WiFiSwitch(wifiManager)
) {
    fun wiFiEnabled(): Boolean =
        try {
            wifiManager.isWifiEnabled
        } catch (e: Exception) {
            false
        }

    fun enableWiFi(): Boolean =
        try {
            wiFiEnabled() || wiFiSwitch.on()
        } catch (e: Exception) {
            false
        }

    fun disableWiFi(): Boolean =
        try {
            !wiFiEnabled() || wiFiSwitch.off()
        } catch (e: Exception) {
            false
        }

    @Suppress("DEPRECATION")
    fun startScan(): Boolean =
        try {
            wifiManager.startScan()
        } catch (e: Exception) {
            false
        }

    @SuppressLint("MissingPermission")
    fun scanResults(): List<ScanResult> =
        try {
            wifiManager.scanResults ?: listOf()
        } catch (e: Exception) {
            listOf()
        }

    @Suppress("DEPRECATION")
    fun wiFiInfo(): WifiInfo? =
        try {
            wifiManager.connectionInfo
        } catch (e: Exception) {
            null
        }


    fun is5GHzBandSupported(): Boolean =
        wifiManager.is5GHzBandSupported

    fun is6GHzBandSupported(): Boolean =
        if (minVersionR()) {
            wifiManager.is6GHzBandSupported
        } else {
            false
        }

    fun minVersionR(): Boolean = buildMinVersionR()

}

