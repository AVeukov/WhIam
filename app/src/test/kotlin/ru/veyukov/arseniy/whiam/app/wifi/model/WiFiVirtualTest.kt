

package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.util.EMPTY
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.*

class WiFiVirtualTest {

    @Test
    fun testWiFiVirtualWithVirtualBSSID() {
        // setup
        val wiFiDetail = WiFiDetail(
                WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
                String.EMPTY,
                WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertEquals(":cf:30:ce:1d:7", actual.bssid)
        assertEquals(2432, actual.frequency)
        assertEquals(":cf:30:ce:1d:7-" + 2432, actual.key)
    }

    @Test
    fun testWiFiVirtualWithRegularBSSIDWhenBSSIDShort() {
        // setup
        val wiFiDetail = WiFiDetail(
                WiFiIdentifier("SSID1", "20:cf:30:ce:1d:7"),
                String.EMPTY,
                WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertEquals("20:cf:30:ce:1d:7", actual.bssid)
        assertEquals(2432, actual.frequency)
        assertEquals("20:cf:30:ce:1d:7-" + 2432, actual.key)
    }

    @Test
    fun testWiFiVirtualWithRegularBSSIDWhenBSSIDLong() {
        // setup
        val wiFiDetail = WiFiDetail(
                WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71:"),
                String.EMPTY,
                WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertEquals("20:cf:30:ce:1d:71:", actual.bssid)
        assertEquals(2432, actual.frequency)
        assertEquals("20:cf:30:ce:1d:71:-" + 2432, actual.key)
    }

}