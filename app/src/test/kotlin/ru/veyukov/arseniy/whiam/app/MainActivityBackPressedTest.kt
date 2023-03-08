

package ru.veyukov.arseniy.whiam.app

import android.os.Build
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.settings.Settings
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainActivityBackPressed

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class MainActivityBackPressedTest {
    private val mainActivity: MainActivity = mock()
    private val menuItem: MenuItem = mock()
    private val settings: Settings = MainContextHelper.INSTANCE.settings
    private val fixture = MainActivityBackPressed(mainActivity)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(settings)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun handleOnBackPressedWhenCloseDrawerIsClosed() {
        // setup
        whenever(mainActivity.closeDrawer()).thenReturn(true)
        // execute
        fixture.handleOnBackPressed()
        // validate
        verify(mainActivity).closeDrawer()
    }

    @Test
    fun handleOnBackPressedWillFinishMainActivity() {
        // setup
        whenever(mainActivity.closeDrawer()).thenReturn(false)
        whenever(settings.selectedMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        whenever(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        // execute
        fixture.handleOnBackPressed()
        // validate
        verify(mainActivity).closeDrawer()
        verify(settings).selectedMenu()
        verify(mainActivity).currentNavigationMenu()
        verify(mainActivity).finish()
    }

    @Test
    fun handleOnBackPressedWillSwitchToPreviousMenu() {
        // setup
        whenever(mainActivity.closeDrawer()).thenReturn(false)
        whenever(settings.selectedMenu()).thenReturn(NavigationMenu.ACCESS_POINTS)
        whenever(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.CHANNEL_GRAPH)
        whenever(mainActivity.currentMenuItem()).thenReturn(menuItem)
        // execute
        fixture.handleOnBackPressed()
        // validate
        verify(mainActivity).closeDrawer()
        verify(settings).selectedMenu()
        verify(mainActivity).currentNavigationMenu()
        verify(mainActivity).currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
        verify(mainActivity).currentMenuItem()
        verify(mainActivity).onNavigationItemSelected(menuItem)
    }

}