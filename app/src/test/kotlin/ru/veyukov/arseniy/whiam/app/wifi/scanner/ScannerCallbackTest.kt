
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import org.junit.After
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.scanner.Cache
import ru.veyukov.arseniy.whiam.wifi.scanner.Scanner
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerCallback

class ScannerCallbackTest {
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val cache: Cache = mock()
    private val scanner: Scanner = mock()
    private val wifiInfo: WifiInfo = mock()
    private val scanResults: List<ScanResult> = listOf()
    private val fixture: ScannerCallback = ScannerCallback(wiFiManagerWrapper, cache)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(cache)
        verifyNoMoreInteractions(scanner)
        verifyNoMoreInteractions(wiFiManagerWrapper)
    }

    @Test
    fun testOnSuccess() {
        // setup
        whenever(wiFiManagerWrapper.scanResults()).thenReturn(scanResults)
        whenever(wiFiManagerWrapper.wiFiInfo()).thenReturn(wifiInfo)
        // execute
        fixture.onSuccess()
        // validate
        verify(wiFiManagerWrapper).scanResults()
        verify(wiFiManagerWrapper).wiFiInfo()
        verify(cache).add(scanResults)
        verify(cache).wifiInfo = wifiInfo
    }

}