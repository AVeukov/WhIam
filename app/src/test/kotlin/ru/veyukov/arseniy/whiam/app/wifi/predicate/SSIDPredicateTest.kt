
package ru.veyukov.arseniy.whiam.wifi.predicate

import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.model.WiFiIdentifier
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.predicate.predicate

class SSIDPredicateTest {
    @Test
    fun testSSIDPredicate() {
        // setup
        val wiFiDetail = WiFiDetail(WiFiIdentifier("ssid", "bssid"), "wpa")
        // execute & validate
        assertTrue("ssid".predicate()(wiFiDetail))
        assertTrue("id".predicate()(wiFiDetail))
        assertTrue("ss".predicate()(wiFiDetail))
        assertTrue("s".predicate()(wiFiDetail))
        assertTrue("".predicate()(wiFiDetail))
        assertFalse("SSID".predicate()(wiFiDetail))
        assertFalse("B".predicate()(wiFiDetail))
    }
}