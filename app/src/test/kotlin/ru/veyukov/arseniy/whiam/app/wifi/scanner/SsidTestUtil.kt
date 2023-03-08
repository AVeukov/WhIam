
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiSsid
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.util.buildMinVersionT

fun whenSsid(scanResult: ScanResult, ssid: String) {
    if (buildMinVersionT()) {
        val wifiSsid: WifiSsid = mock()
        whenever(scanResult.wifiSsid).thenReturn(wifiSsid)
        whenever(wifiSsid.toString()).thenReturn(ssid)
    } else {
        @Suppress("DEPRECATION")
        scanResult.SSID = ssid
    }
}
