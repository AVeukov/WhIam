
package ru.veyukov.arseniy.whiam.wifi.manager

import android.net.wifi.WifiManager
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiSwitch

class WiFiSwitchTest {
    private val wifiManager: WifiManager = mock()
    private val fixture = spy(WiFiSwitch(wifiManager))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiManager)
    }

    @Suppress("DEPRECATION")
    @Test
    fun testOn() {
        // setup
        whenever(wifiManager.setWifiEnabled(true)).thenReturn(true)
        // execute
        val actual = fixture.on()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled = true
    }

    @Suppress("DEPRECATION")
    @Test
    fun testOff() {
        // setup
        whenever(wifiManager.setWifiEnabled(false)).thenReturn(true)
        // execute
        val actual = fixture.off()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled = false
    }

    @Test
    fun testOnWithAndroidQ() {
        // setup
        doReturn(true).whenever(fixture).minVersionQ()
        doNothing().whenever(fixture).startWiFiSettings()
        // execute
        val actual = fixture.on()
        // validate
        assertTrue(actual)
        verify(fixture).startWiFiSettings()
        verify(fixture).minVersionQ()
    }
}