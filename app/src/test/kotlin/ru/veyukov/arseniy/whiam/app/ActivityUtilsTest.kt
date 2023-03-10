
package ru.veyukov.arseniy.whiam.app

import android.content.Intent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.ActionBar
import androidx.appcompat.widget.Toolbar
import com.nhaarman.mockitokotlin2.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.keepScreenOn
import ru.veyukov.arseniy.whiam.setupToolbar
import ru.veyukov.arseniy.whiam.startLocationSettings
import ru.veyukov.arseniy.whiam.startWiFiSettings

class ActivityUtilsTest {
    private val window: Window = mock()
    private val actionBar: ActionBar = mock()
    private val toolbar: Toolbar = mock()
    private val intent: Intent = mock()
    private val mainActivity = MainContextHelper.INSTANCE.mainActivity
    private val settings = MainContextHelper.INSTANCE.settings

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(toolbar)
        verifyNoMoreInteractions(actionBar)
        verifyNoMoreInteractions(window)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(intent)
    }

    @Test
    fun testSetupToolbar() {
        // setup
        whenever<Any>(mainActivity.findViewById(R.id.toolbar)).thenReturn(toolbar)
        whenever(mainActivity.supportActionBar).thenReturn(actionBar)
        // execute
        val actual = mainActivity.setupToolbar()
        // validate
        assertEquals(toolbar, actual)
        verify(mainActivity).findViewById<View>(R.id.toolbar)
        verify(mainActivity).supportActionBar
        verify(mainActivity).setSupportActionBar(toolbar)
        verify(actionBar).setHomeButtonEnabled(true)
        verify(actionBar).setDisplayHomeAsUpEnabled(true)
    }

    @Test
    fun testKeepScreenOnSwitchOn() {
        // setup
        whenever(settings.keepScreenOn()).thenReturn(true)
        whenever(mainActivity.window).thenReturn(window)
        // execute
        mainActivity.keepScreenOn()
        // validate
        verify(settings).keepScreenOn()
        verify(mainActivity).window
        verify(window).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Test
    fun testKeepScreenOnSwitchOff() {
        // setup
        whenever(settings.keepScreenOn()).thenReturn(false)
        whenever(mainActivity.window).thenReturn(window)
        // execute
        mainActivity.keepScreenOn()
        // validate
        verify(settings).keepScreenOn()
        verify(mainActivity).window
        verify(window).clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Test
    fun testStartWiFiSettings() {
        // execute
        mainActivity.startWiFiSettings()
        // validate
        verify(mainActivity).startActivity(any())
    }

    @Test
    fun testStartLocationSettings() {
        // execute
        mainActivity.startLocationSettings()
        // validate
        verify(mainActivity).startActivity(any())
    }
}