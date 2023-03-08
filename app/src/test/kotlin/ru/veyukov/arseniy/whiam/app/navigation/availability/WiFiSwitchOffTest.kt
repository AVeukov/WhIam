
package ru.veyukov.arseniy.whiam.navigation.availability

import android.view.Menu
import android.view.MenuItem
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import org.junit.After
import org.junit.Test
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionWiFiSwitchOff

class WiFiSwitchOffTest {
    private val mainActivity = MainContextHelper.INSTANCE.mainActivity
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithMenuWillSetVisibility() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_wifi_band)).thenReturn(menuItem)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_wifi_band)
        verify(menuItem).isVisible = false
    }

    @Test
    fun testNavigationOptionWiFiSwitchOffWithNoMenuWillNotVisibility() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionWiFiSwitchOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_wifi_band)
        verify(menuItem, never()).isVisible = any()
    }

}