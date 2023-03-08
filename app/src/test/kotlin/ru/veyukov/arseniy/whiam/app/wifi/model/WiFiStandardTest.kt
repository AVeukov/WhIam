

package ru.veyukov.arseniy.whiam.wifi.model

import android.net.wifi.ScanResult
import ru.veyukov.arseniy.whiam.R
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.WiFiStandard

class WiFiStandardTest {
    @Test
    fun testWidth() {
        assertEquals(7, WiFiStandard.values().size)
    }

    @Test
    fun testNameResource() {
        assertEquals(R.string.wifi_standard_unknown, WiFiStandard.UNKNOWN.textResource)
        assertEquals(R.string.wifi_standard_legacy, WiFiStandard.LEGACY.textResource)
        assertEquals(R.string.wifi_standard_n, WiFiStandard.N.textResource)
        assertEquals(R.string.wifi_standard_ac, WiFiStandard.AC.textResource)
        assertEquals(R.string.wifi_standard_ax, WiFiStandard.AX.textResource)
        assertEquals(R.string.wifi_standard_ad, WiFiStandard.AD.textResource)
        assertEquals(R.string.wifi_standard_be, WiFiStandard.BE.textResource)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.UNKNOWN.imageResource)
        assertEquals(R.drawable.ic_wifi_legacy, WiFiStandard.LEGACY.imageResource)
        assertEquals(R.drawable.ic_wifi_4, WiFiStandard.N.imageResource)
        assertEquals(R.drawable.ic_wifi_5, WiFiStandard.AC.imageResource)
        assertEquals(R.drawable.ic_wifi_6, WiFiStandard.AX.imageResource)
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.AD.imageResource)
        assertEquals(R.drawable.ic_wifi_unknown, WiFiStandard.BE.imageResource)
    }

    @Test
    fun testWiFIStandard() {
        assertEquals(ScanResult.WIFI_STANDARD_UNKNOWN, WiFiStandard.UNKNOWN.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_LEGACY, WiFiStandard.LEGACY.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11N, WiFiStandard.N.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11AC, WiFiStandard.AC.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11AX, WiFiStandard.AX.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11AD, WiFiStandard.AD.wiFiStandardId)
        assertEquals(ScanResult.WIFI_STANDARD_11BE, WiFiStandard.BE.wiFiStandardId)
    }

    @Test
    fun testFindOne() {
        assertEquals(WiFiStandard.UNKNOWN, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_UNKNOWN))
        assertEquals(WiFiStandard.LEGACY, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_LEGACY))
        assertEquals(WiFiStandard.N, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11N))
        assertEquals(WiFiStandard.AC, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11AC))
        assertEquals(WiFiStandard.AX, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11AX))
        assertEquals(WiFiStandard.AD, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11AD))
        assertEquals(WiFiStandard.BE, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11BE))
        assertEquals(WiFiStandard.UNKNOWN, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_UNKNOWN - 1))
        assertEquals(WiFiStandard.UNKNOWN, WiFiStandard.findOne(ScanResult.WIFI_STANDARD_11BE + 1))
    }

}