

package ru.veyukov.arseniy.whiam.app

import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannel
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannelPair
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannels
import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.Configuration
import ru.veyukov.arseniy.whiam.SIZE_MIN
import java.util.*

class ConfigurationTest {
    private val fixture = Configuration(true)

    @Test
    fun testSizeAvailable() {
        // execute & validate
        assertTrue(fixture.sizeAvailable)
    }

    @Test
    fun testSizeIsNotAvailable() {
        // execute
        fixture.size = SIZE_MIN
        // validate
        assertFalse(fixture.sizeAvailable)
    }

    @Test
    fun testLargeScreen() {
        // execute & validate
        assertTrue(fixture.largeScreen)
    }

    @Test
    fun testWiFiChannelPairWithInit() {
        // execute & validate
        WiFiBand.values().forEach {
            assertEquals(WiFiChannels.UNKNOWN, fixture.wiFiChannelPair(it))
        }
    }

    @Test
    fun testWiFiChannelPairWithCountry() {
        // execute
        fixture.wiFiChannelPair(Locale.US.country)
        // validate
        WiFiBand.values().forEach {
            assertEquals(it.wiFiChannels.wiFiChannelPairFirst(Locale.US.country), fixture.wiFiChannelPair(it))
        }
    }

    @Test
    fun testWiFiChannelPairWithWiFiBand() {
        // setup
        val expected = WiFiChannelPair(WiFiChannel(1, 2), WiFiChannel(3, 4))
        // execute
        fixture.wiFiChannelPair(WiFiBand.GHZ5, expected)
        // validate
        assertEquals(expected, fixture.wiFiChannelPair(WiFiBand.GHZ5))
    }

}
