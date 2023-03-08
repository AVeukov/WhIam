
package ru.veyukov.arseniy.whiam.wifi.predicate

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.*
import ru.veyukov.arseniy.whiam.wifi.predicate.predicate

class StrengthPredicateTest {
    @Test
    fun testStrengthPredicate() {
        // setup
        val wiFiDetail = makeWiFiDetail(-60)
        // execute & validate
        assertTrue(Strength.THREE.predicate()(wiFiDetail))
        assertFalse(Strength.FOUR.predicate()(wiFiDetail))
    }

    private fun makeWiFiDetail(level: Int): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier("ssid", "bssid"),
            "wpa",
            WiFiSignal(2445, 2445, WiFiWidth.MHZ_20, level, true)
        )
}