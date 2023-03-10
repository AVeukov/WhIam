
package ru.veyukov.arseniy.whiam.wifi.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.WiFiAdditional
import ru.veyukov.arseniy.whiam.wifi.model.WiFiConnection
import ru.veyukov.arseniy.whiam.wifi.model.WiFiIdentifier

class WiFiAdditionalTest {
    private val vendorName = "VendorName"

    @Test
    fun testWiFiAdditionalWithWiFiConnection() {
        // setup
        val wiFiConnection = WiFiConnection(WiFiIdentifier("SSID", "BSSID"), "192.168.1.10", 22)
        // execute
        val fixture = WiFiAdditional(vendorName, wiFiConnection)
        // validate
        assertEquals(vendorName, fixture.vendorName)
        assertEquals(wiFiConnection, fixture.wiFiConnection)
    }

    @Test
    fun testWiFiAdditional() {
        // execute
        val fixture = WiFiAdditional(vendorName, WiFiConnection.EMPTY)
        // validate
        assertEquals(vendorName, fixture.vendorName)
    }

    @Test
    fun testWiFiAdditionalEmpty() {
        // validate
        assertTrue(WiFiAdditional.EMPTY.vendorName.isEmpty())
    }

}