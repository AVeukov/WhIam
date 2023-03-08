
package ru.veyukov.arseniy.whiam.wifi.band

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand.Companion.find
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.band.availableGHZ2
import ru.veyukov.arseniy.whiam.wifi.band.availableGHZ5
import ru.veyukov.arseniy.whiam.wifi.band.availableGHZ6

class WiFiBandTest {
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(wiFiManagerWrapper)
    }


    @Test
    fun testWiFiBand() {
        assertEquals(3, WiFiBand.values().size)
    }

    @Test
    fun testAvailable() {
        assertTrue(WiFiBand.GHZ2.available.javaClass.isInstance(availableGHZ2))
        assertTrue(WiFiBand.GHZ5.available.javaClass.isInstance(availableGHZ5))
        assertTrue(WiFiBand.GHZ6.available.javaClass.isInstance(availableGHZ6))
    }

    @Test
    fun testTextResource() {
        assertEquals(R.string.wifi_band_2ghz, WiFiBand.GHZ2.textResource)
        assertEquals(R.string.wifi_band_5ghz, WiFiBand.GHZ5.textResource)
        assertEquals(R.string.wifi_band_6ghz, WiFiBand.GHZ6.textResource)
    }

    @Test
    fun testGhz5() {
        assertFalse(WiFiBand.GHZ2.ghz5)
        assertTrue(WiFiBand.GHZ5.ghz5)
        assertFalse(WiFiBand.GHZ6.ghz5)
    }

    @Test
    fun testGhz2() {
        assertTrue(WiFiBand.GHZ2.ghz2)
        assertFalse(WiFiBand.GHZ5.ghz2)
        assertFalse(WiFiBand.GHZ6.ghz2)
    }

    @Test
    fun testGhz6() {
        assertFalse(WiFiBand.GHZ2.ghz6)
        assertFalse(WiFiBand.GHZ5.ghz6)
        assertTrue(WiFiBand.GHZ6.ghz6)
    }

    @Test
    fun testWiFiBandFind() {
        assertEquals(WiFiBand.GHZ2, find(2399))
        assertEquals(WiFiBand.GHZ2, find(2400))
        assertEquals(WiFiBand.GHZ2, find(2499))
        assertEquals(WiFiBand.GHZ2, find(2500))

        assertEquals(WiFiBand.GHZ2, find(4899))
        assertEquals(WiFiBand.GHZ5, find(4900))
        assertEquals(WiFiBand.GHZ5, find(5899))
        assertEquals(WiFiBand.GHZ2, find(5900))

        assertEquals(WiFiBand.GHZ2, find(5924))
        assertEquals(WiFiBand.GHZ6, find(5925))
        assertEquals(WiFiBand.GHZ6, find(7125))
        assertEquals(WiFiBand.GHZ2, find(7126))
    }

    @Test
    fun testAvailableGHZ2() {
        // execute
        val actual = WiFiBand.GHZ2.available()
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAvailableGHZ5() {
        // setup
        whenever(wiFiManagerWrapper.is5GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.GHZ5.available()
        // validate
        assertTrue(actual)
        verify(wiFiManagerWrapper).is5GHzBandSupported()
    }

    @Test
    fun testAvailableGHZ6() {
        // setup
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.GHZ6.available()
        // validate
        assertTrue(actual)
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }

}