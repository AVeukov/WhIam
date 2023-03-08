
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.util.buildMinVersionR
import ru.veyukov.arseniy.whiam.util.ssid
import ru.veyukov.arseniy.whiam.wifi.model.*

@Suppress("DEPRECATION")
fun WifiInfo.ipV4Address(): Int = ipAddress

@OpenClass
internal class Transformer(private val cache: Cache) {

    internal fun transformWifiInfo(): WiFiConnection {
        val wifiInfo: WifiInfo? = cache.wifiInfo
        return if (wifiInfo == null || wifiInfo.networkId == -1) {
            WiFiConnection.EMPTY
        } else {
            val ssid = convertSSID(wifiInfo.ssid ?: String.EMPTY)
            val wiFiIdentifier = WiFiIdentifier(ssid, wifiInfo.bssid ?: String.EMPTY)
            WiFiConnection(wiFiIdentifier, convertIpV4Address(wifiInfo.ipV4Address()), wifiInfo.linkSpeed)
        }
    }

    internal fun transformCacheResults(): List<WiFiDetail> =
        cache.scanResults().map { transform(it) }

    internal fun transformToWiFiData(): WiFiData =
        WiFiData(transformCacheResults(), transformWifiInfo())

    internal fun wiFiStandard(scanResult: ScanResult): WiFiStandardId =
        if (minVersionR()) {
            scanResult.wifiStandard
        } else {
            WiFiStandard.UNKNOWN.wiFiStandardId
        }

    internal fun minVersionR(): Boolean = buildMinVersionR()

    private fun transform(cacheResult: CacheResult): WiFiDetail {
        val scanResult = cacheResult.scanResult
        val wiFiWidth = WiFiWidth.findOne(scanResult.channelWidth)
        val centerFrequency = wiFiWidth.calculateCenter(scanResult.frequency, scanResult.centerFreq0)
        val mc80211 = scanResult.is80211mcResponder
        val wiFiStandard = WiFiStandard.findOne(wiFiStandard(scanResult))
        val wiFiSignal = WiFiSignal(
            scanResult.frequency, centerFrequency, wiFiWidth,
            cacheResult.average, mc80211, wiFiStandard, scanResult.timestamp
        )
        val wiFiIdentifier = WiFiIdentifier(
            scanResult.ssid(),
            if (scanResult.BSSID == null) String.EMPTY else scanResult.BSSID
        )
        return WiFiDetail(
            wiFiIdentifier,
            if (scanResult.capabilities == null) String.EMPTY else scanResult.capabilities,
            wiFiSignal
        )
    }

}