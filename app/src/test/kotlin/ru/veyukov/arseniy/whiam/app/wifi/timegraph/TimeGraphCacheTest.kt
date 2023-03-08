
package ru.veyukov.arseniy.whiam.wifi.timegraph

import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.wifi.graphutils.MAX_NOT_SEEN_COUNT
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.model.WiFiIdentifier
import ru.veyukov.arseniy.whiam.wifi.model.WiFiSignal
import ru.veyukov.arseniy.whiam.wifi.model.WiFiWidth
import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.timegraph.TimeGraphCache

class TimeGraphCacheTest {
    private val fixture = TimeGraphCache()

    @Test
    fun testAll() {
        // setup
        val expected = withWiFiDetails()
        // execute
        val actual = fixture.wiFiDetails
        // validate
        assertEquals(expected.size, actual.size)
    }

    @Test
    fun testActive() {
        // setup
        val expected = withWiFiDetails()
        // execute
        val actual = fixture.active()
        // validate
        assertEquals(expected.size - 1, actual.size)
        assertFalse(actual.contains(expected[0]))
    }

    @Test
    fun testClear() {
        // setup
        val expected = withWiFiDetails()
        // execute
        fixture.clear()
        // validate
        val actual = fixture.wiFiDetails
        assertEquals(expected.size - 1, actual.size)
        assertFalse(actual.contains(expected[0]))
    }

    @Test
    fun testReset() {
        // setup
        val expected = withWiFiDetails()
        // execute
        fixture.reset(expected[0])
        // validate
        val actual = fixture.wiFiDetails
        assertEquals(expected.size, actual.size)
        assertTrue(actual.contains(expected[0]))
    }

    private fun withWiFiDetail(SSID: String): WiFiDetail {
        return WiFiDetail(
                WiFiIdentifier(SSID, "BSSID"),
                String.EMPTY,
                WiFiSignal(100, 100, WiFiWidth.MHZ_20, 5, true))
    }

    private fun withWiFiDetails(): List<WiFiDetail> {
        val results: MutableList<WiFiDetail> = mutableListOf()
        for (i in 0..3) {
            val wiFiDetail = withWiFiDetail("SSID$i")
            results.add(wiFiDetail)
        }
        results.forEach { fixture.add(it) }
        for (i in 0 until MAX_NOT_SEEN_COUNT) {
            fixture.add(results[0])
        }
        return results
    }

}