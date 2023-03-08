
package ru.veyukov.arseniy.whiam.navigation.availability

import android.view.Menu
import android.view.MenuItem
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import org.junit.After
import org.junit.Test
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionFilterOff

class FilterOffTest {
    private val mainActivity: MainActivity = mock()
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(menuItem)
    }

    @Test
    fun testNavigationOptionFilterOff() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_filter)).thenReturn(menuItem)
        // execute
        navigationOptionFilterOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_filter)
        verify(menuItem).isVisible = false
    }

    @Test
    fun testNavigationOptionFilterOffWithNoMenuDoesNotSetVisibleFalse() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionFilterOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_filter)
        verify(menuItem, never()).isVisible = any()
    }
}