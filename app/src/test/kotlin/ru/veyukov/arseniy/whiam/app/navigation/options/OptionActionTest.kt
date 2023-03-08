
package ru.veyukov.arseniy.whiam.navigation.options

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContextHelper.INSTANCE
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.navigation.options.OptionAction.Companion.findOptionAction
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.navigation.options.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class OptionActionTest {
    private val mainActivity: MainActivity = RobolectricUtil.INSTANCE.activity
    private val scannerService: ScannerService = INSTANCE.scannerService
    private val settings: Settings = INSTANCE.settings

    @After
    fun tearDown() {
        verifyNoMoreInteractions(scannerService)
        verifyNoMoreInteractions(settings)
        INSTANCE.restore()
    }

    @Test
    fun testScannerAction() {
        // execute
        scannerAction()
        // validate
        verify(scannerService).toggle()
    }

    @Test
    fun testWiFiBandAction2() {
        // execute
        wiFiBandAction2()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ2)
    }

    @Test
    fun testWiFiBandAction5() {
        // execute
        wiFiBandAction5()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ5)
    }

    @Test
    fun testWiFiBandAction6() {
        // execute
        wiFiBandAction6()
        // validate
        verify(settings).wiFiBand(WiFiBand.GHZ6)
    }

    @Test
    fun testFilterAction() {
        filterAction()
    }

    @Test
    fun testOptionAction() {
        assertEquals(6, OptionAction.values().size)
    }

    @Test
    fun testGetKey() {
        assertEquals(-1, OptionAction.NO_ACTION.key)
        assertEquals(R.id.action_scanner, OptionAction.SCANNER.key)
        assertEquals(R.id.action_filter, OptionAction.FILTER.key)
        assertEquals(R.id.action_wifi_band_2ghz, OptionAction.WIFI_BAND_2.key)
        assertEquals(R.id.action_wifi_band_5ghz, OptionAction.WIFI_BAND_5.key)
        assertEquals(R.id.action_wifi_band_6ghz, OptionAction.WIFI_BAND_6.key)
    }

    @Test
    fun testGetAction() {
        assertTrue(OptionAction.NO_ACTION.action == noAction)
        assertTrue(OptionAction.SCANNER.action == scannerAction)
        assertTrue(OptionAction.FILTER.action == filterAction)
        assertTrue(OptionAction.WIFI_BAND_2.action == wiFiBandAction2)
        assertTrue(OptionAction.WIFI_BAND_5.action == wiFiBandAction5)
        assertTrue(OptionAction.WIFI_BAND_6.action == wiFiBandAction6)
    }

    @Test
    fun testGetOptionAction() {
        assertEquals(OptionAction.NO_ACTION, findOptionAction(OptionAction.NO_ACTION.key))
        assertEquals(OptionAction.SCANNER, findOptionAction(OptionAction.SCANNER.key))
        assertEquals(OptionAction.FILTER, findOptionAction(OptionAction.FILTER.key))
        assertEquals(OptionAction.WIFI_BAND_2, findOptionAction(OptionAction.WIFI_BAND_2.key))
        assertEquals(OptionAction.WIFI_BAND_5, findOptionAction(OptionAction.WIFI_BAND_5.key))
        assertEquals(OptionAction.WIFI_BAND_6, findOptionAction(OptionAction.WIFI_BAND_6.key))
    }

    @Test
    fun testGetOptionActionInvalidKey() {
        assertEquals(OptionAction.NO_ACTION, findOptionAction(-99))
        assertEquals(OptionAction.NO_ACTION, findOptionAction(99))
    }
}