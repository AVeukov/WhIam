
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import ru.veyukov.arseniy.whiam.wifi.scanner.Callback
import ru.veyukov.arseniy.whiam.wifi.scanner.ScanResultsReceiver

class ScanResultsReceiverTest {
    private val mainActivity: MainActivity = mock()
    private val callback: Callback = mock()
    private val intentFilter: IntentFilter = mock()
    private val intent: Intent = mock()
    private val fixture: ScanResultsReceiver = spy(ScanResultsReceiver(mainActivity, callback))

    @Before
    fun setUp() {
        whenever(fixture.makeIntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)).thenReturn(intentFilter)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(callback)
    }

    @Test
    fun testRegisterOnce() {
        // execute
        fixture.register()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
    }

    @Test
    fun testRegisterMoreThanOnce() {
        // execute
        fixture.register()
        fixture.register()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
    }

    @Test
    fun testUnregisterOnce() {
        // setup
        fixture.register()
        // execute
        fixture.unregister()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
        verify(mainActivity).unregisterReceiver(fixture)
    }

    @Test
    fun testUnregisterMoreThanOnce() {
        // setup
        fixture.register()
        // execute
        fixture.unregister()
        fixture.unregister()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
        verify(mainActivity).unregisterReceiver(fixture)
    }

    @Test
    fun testOnReceiveWithScanResultsAction() {
        // setup
        whenever(intent.action).thenReturn(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        whenever(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)).thenReturn(true)
        // execute
        fixture.onReceive(mainActivity, intent)
        // verify
        verify(intent).action
        verify(intent).getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        verify(callback).onSuccess()
    }

    @Test
    fun testOnReceiveWithSomeOtherAction() {
        // setup
        whenever(intent.action).thenReturn(WifiManager.ACTION_PICK_WIFI_NETWORK)
        // execute
        fixture.onReceive(mainActivity, intent)
        // verify
        verify(intent).action
        verify(intent, never()).getBooleanExtra(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())
        verify(callback, never()).onSuccess()
    }

    @Test
    fun testOnReceiveWithBooleanExtraFalse() {
        // setup
        whenever(intent.action).thenReturn(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        whenever(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)).thenReturn(false)
        // execute
        fixture.onReceive(mainActivity, intent)
        // verify
        verify(intent).action
        verify(intent).getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        verify(callback, never()).onSuccess()
    }
}