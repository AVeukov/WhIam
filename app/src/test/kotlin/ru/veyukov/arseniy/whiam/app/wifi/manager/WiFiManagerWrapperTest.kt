
package ru.veyukov.arseniy.whiam.wifi.manager

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiSwitch

class WiFiManagerWrapperTest {
    private val wifiManager: WifiManager = mock()
    private val wiFiSwitch: WiFiSwitch = mock()
    private val wifiInfo: WifiInfo = mock()
    private val fixture = spy(WiFiManagerWrapper(wifiManager, wiFiSwitch))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wifiManager)
        verifyNoMoreInteractions(wiFiSwitch)
    }

    @Test
    fun testWiFiEnabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // execute
        val actual = fixture.wiFiEnabled()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun testWiFiEnabledWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenThrow(RuntimeException())
        // execute
        val actual = fixture.wiFiEnabled()
        // validate
        assertFalse(actual)
        verify(wifiManager).isWifiEnabled
    }

    @Test
    @Suppress("DEPRECATION")
    fun testEnableWiFi() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
    }

    @Test
    fun testEnableWiFiWhenDisabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenReturn(true)
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun testEnableWiFiWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        whenever(wiFiSwitch.on()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.enableWiFi()
        // validate
        assertFalse(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).on()
    }

    @Test
    fun testDisableWiFi() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenReturn(true)
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Test
    fun testDisableWiFiWhenDisabled() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(false)
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertTrue(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch, never()).off()
    }

    @Test
    fun testDisableWiFiWithException() {
        // setup
        whenever(wifiManager.isWifiEnabled).thenReturn(true)
        whenever(wiFiSwitch.off()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.disableWiFi()
        // validate
        assertFalse(actual)
        verify(wifiManager).isWifiEnabled
        verify(wiFiSwitch).off()
    }

    @Suppress("DEPRECATION")
    @Test
    fun testStartScan() {
        // setup
        whenever(wifiManager.startScan()).thenReturn(true)
        // execute
        val actual = fixture.startScan()
        // validate
        assertTrue(actual)
        verify(wifiManager).startScan()
    }

    @Suppress("DEPRECATION")
    @Test
    fun testStartScanWithException() {
        // setup
        whenever(wifiManager.startScan()).thenThrow(RuntimeException())
        // execute
        val actual = fixture.startScan()
        // validate
        assertFalse(actual)
        verify(wifiManager).startScan()
    }

    @Test
    fun testScanResults() {
        // setup
        val expected = listOf<ScanResult>()
        whenever(wifiManager.scanResults).thenReturn(expected)
        // execute
        val actual = fixture.scanResults()
        // validate
        assertSame(expected, actual)
        verify(wifiManager).scanResults
    }

    @Test
    fun testScanResultsWhenWiFiManagerReturnsNullScanResults() {
        // setup
        whenever(wifiManager.scanResults).thenReturn(null)
        // execute
        val actual = fixture.scanResults()
        // validate
        assertNotNull(actual)
        assertTrue(actual.isEmpty())
        verify(wifiManager).scanResults
    }

    @Test
    fun testScanResultsWithException() {
        // setup
        whenever(wifiManager.scanResults).thenThrow(RuntimeException())
        // execute
        val actual = fixture.scanResults()
        // validate
        assertNotNull(actual)
        assertTrue(actual.isEmpty())
        verify(wifiManager).scanResults
    }

    @Suppress("DEPRECATION")
    @Test
    fun testWiFiInfo() {
        // setup
        whenever(wifiManager.connectionInfo).thenReturn(wifiInfo)
        // execute
        val actual = fixture.wiFiInfo()
        // validate
        assertSame(wifiInfo, actual)
        verify(wifiManager).connectionInfo
    }

    @Suppress("DEPRECATION")
    @Test
    fun testWiFiInfoWithException() {
        // setup
        whenever(wifiManager.connectionInfo).thenThrow(RuntimeException())
        // execute
        val actual = fixture.wiFiInfo()
        // validate
        assertNull(actual)
        verify(wifiManager).connectionInfo
    }

    @Test
    fun testIs5GHzBandSupported() {
        // setup
        whenever(wifiManager.is5GHzBandSupported).thenReturn(true)
        // execute
        val actual = fixture.is5GHzBandSupported()
        // validate
        assertTrue(actual)
        verify(wifiManager).is5GHzBandSupported
    }

    @Test
    fun testIs6GHzBandSupported() {
        // setup
        doReturn(false).whenever(fixture).minVersionR()
        // execute
        val actual = fixture.is6GHzBandSupported()
        // validate
        assertFalse(actual)
        verify(wifiManager, never()).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

    @Test
    fun testIs6GHzBandSupportedWithAndroidR() {
        // setup
        doReturn(true).whenever(fixture).minVersionR()
        whenever(wifiManager.is6GHzBandSupported).thenReturn(true)
        // execute
        val actual = fixture.is6GHzBandSupported()
        // validate
        assertTrue(actual)
        verify(wifiManager).is6GHzBandSupported
        verify(fixture).minVersionR()
    }

}