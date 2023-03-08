
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.os.Handler
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.settings.Settings
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.scanner.PeriodicScan
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService

class PeriodicScanTest {
    private val handler: Handler = mock()
    private val settings: Settings = mock()
    private val scanner: ScannerService = mock()
    private val fixture: PeriodicScan = PeriodicScan(scanner, handler, settings)

    @Test
    fun testRun() {
        // setup
        val delayInterval = 1000L
        val scanSpeed = 15
        whenever(settings.scanSpeed()).thenReturn(scanSpeed)
        // execute
        fixture.run()
        // validate
        verify(scanner).update()
        verify(handler).removeCallbacks(fixture)
        verify(handler).postDelayed(fixture, scanSpeed * delayInterval)
    }

    @Test
    fun testStop() {
        // execute
        fixture.stop()
        // validate
        verify(handler).removeCallbacks(fixture)
    }

    @Test
    fun testStart() {
        // setup
        val delayInitial = 1L
        // execute
        fixture.start()
        // validate
        verify(handler).removeCallbacks(fixture)
        verify(handler).postDelayed(fixture, delayInitial)
    }

    @Test
    fun testStartWithDelay() {
        // setup
        val scanSpeed = 15
        whenever(settings.scanSpeed()).thenReturn(scanSpeed)
        // execute
        fixture.startWithDelay()
        // validate
        verify(handler).removeCallbacks(fixture)
        verify(handler).postDelayed(fixture, scanSpeed * PeriodicScan.DELAY_INTERVAL)
        verify(settings).scanSpeed()
    }

}