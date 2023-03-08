
package ru.veyukov.arseniy.whiam.wifi.predicate

import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.model.WiFiIdentifier
import ru.veyukov.arseniy.whiam.wifi.model.WiFiSignal
import ru.veyukov.arseniy.whiam.wifi.model.WiFiWidth
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.predicate.predicate

class WiFiBandPredicateTest {
    @Test
    fun testWiFiBandPredicateWith2GHzFrequency() {
        // setup
        val wiFiDetail = makeWiFiDetail(2455)
        // execute & validate
        assertTrue(WiFiBand.GHZ2.predicate()(wiFiDetail))
        assertFalse(WiFiBand.GHZ5.predicate()(wiFiDetail))
    }

    @Test
    fun testWiFiBandPredicateWith5GHzFrequency() {
        // setup
        val wiFiDetail = makeWiFiDetail(5455)
        // execute & validate
        assertFalse(WiFiBand.GHZ2.predicate()(wiFiDetail))
        assertTrue(WiFiBand.GHZ5.predicate()(wiFiDetail))
    }

    private fun makeWiFiDetail(frequency: Int): WiFiDetail =
            WiFiDetail(
                    WiFiIdentifier("ssid", "bssid"),
                    "wpa",
                    WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, 1, true))

}