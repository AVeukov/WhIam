
package ru.veyukov.arseniy.whiam.navigation.availability

import android.view.Menu
import android.view.MenuItem
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import org.junit.After
import org.junit.Test
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionScannerSwitchOff

class ScannerSwitchOffTest {
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
    fun testNavigationOptionScannerSwitchOff() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_scanner)).thenReturn(menuItem)
        // execute
        navigationOptionScannerSwitchOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_scanner)
        verify(menuItem).isVisible = false
    }

    @Test
    fun testNavigationOptionScannerSwitchOffWithNoMenuDoesNotSetVisibleFalse() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionScannerSwitchOff(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu, never()).findItem(R.id.action_scanner)
        verify(menuItem, never()).isVisible = any()
    }
}