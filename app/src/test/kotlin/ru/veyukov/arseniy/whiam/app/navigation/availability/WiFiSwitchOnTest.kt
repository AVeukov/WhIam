
package ru.veyukov.arseniy.whiam.navigation.availability

import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionWiFiSwitchOn

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class WiFiSwitchOnTest {
    private val mainActivity = MainContextHelper.INSTANCE.mainActivity
    private val settings = MainContextHelper.INSTANCE.settings
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testNavigationOptionWiFiSwitchOnWithMenuWillSetTitleAndVisibility() {
        // setup
        val expected = "XYZ\n123"
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_wifi_band)).thenReturn(menuItem)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(mainActivity.getString(WiFiBand.GHZ5.textResource)).thenReturn("XYZ 123")
        // execute
        navigationOptionWiFiSwitchOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_wifi_band)
        verify(settings).wiFiBand()
        verify(mainActivity).getString(WiFiBand.GHZ5.textResource)
        verify(menuItem).isVisible = true
        verify(menuItem).title = expected
    }

    @Test
    fun testNavigationOptionWiFiSwitchOnWithNoMenuWillNotSetTitleAndVisibility() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionWiFiSwitchOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_wifi_band)
        verify(settings, never()).wiFiBand()
        verify(mainActivity, never()).getString(WiFiBand.GHZ5.textResource)
        verify(menuItem, never()).isVisible = any()
        verify(menuItem, never()).title = any()
    }

}