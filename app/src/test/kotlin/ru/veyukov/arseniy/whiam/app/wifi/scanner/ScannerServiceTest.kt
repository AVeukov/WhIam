
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.os.Handler
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.scanner.Scanner
import ru.veyukov.arseniy.whiam.wifi.scanner.makeScannerService

class ScannerServiceTest {
    private val wiFiManagerWrapper: WiFiManagerWrapper = mock()
    private val mainActivity: MainActivity = mock()
    private val handler: Handler = mock()
    private val settings: Settings = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(wiFiManagerWrapper)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(handler)
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun testMakeScannerService() {
        // setup
        // execute
        val actual = makeScannerService(mainActivity, wiFiManagerWrapper, handler, settings) as Scanner
        // validate
        assertEquals(wiFiManagerWrapper, actual.wiFiManagerWrapper)
        assertEquals(settings, actual.settings)
        assertNotNull(actual.transformer)
        assertNotNull(actual.periodicScan)
        assertNotNull(actual.scannerCallback)
        assertNotNull(actual.scanResultsReceiver)
        assertFalse(actual.running())
    }

}