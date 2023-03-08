
package ru.veyukov.arseniy.whiam.app

import android.content.SharedPreferences
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.navigation.NavigationView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.navigation.NavigationMenuController
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import ru.veyukov.arseniy.whiam.permission.PermissionService
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.DrawerNavigation
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContext

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class MainActivityTest {
    private val sharedPreferences: SharedPreferences = mock()
    private val permissionService: PermissionService = mock()
    private val fixture = Robolectric.buildActivity(MainActivity::class.java).create().resume().get()

    @Before
    fun setup() {
        fixture.permissionService = permissionService
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(permissionService)
        verifyNoMoreInteractions(sharedPreferences)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testMainActivity() {
        assertFalse(MainContext.INSTANCE.scannerService.running())
    }

    @Test
    fun testOnPauseWillPauseScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onPause()
        // validate
        verify(scannerService).pause()
        verify(scannerService).unregister(fixture.connectionView)
    }

    @Test
    fun testOnResumeWithPermissionGrantedWillResumeScanner() {
        // setup
        val scannerService = MainContextHelper.INSTANCE.scannerService
        whenever(permissionService.permissionGranted()).thenReturn(true)
        whenever(permissionService.systemEnabled()).thenReturn(false)
        // execute
        fixture.onResume()
        // validate
        verify(permissionService).permissionGranted()
        verify(permissionService).systemEnabled()
        verify(scannerService).resume()
        verify(scannerService).register(fixture.connectionView)
    }

    @Test
    fun testOnResumeWithPermissionNotGrantedWillPauseScanner() {
        // setup
        whenever(permissionService.permissionGranted()).thenReturn(false)
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onResume()
        // validate
        verify(scannerService).pause()
        verify(scannerService).register(fixture.connectionView)
        verify(permissionService).permissionGranted()
    }

    @Test
    fun testOnStartWithPermissionGrantedWillResumeScanner() {
        // setup
        whenever(permissionService.permissionGranted()).thenReturn(true)
        val scannerService = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onStart()
        // validate
        verify(scannerService).resume()
        verify(permissionService).permissionGranted()
    }

    @Test
    fun testOnStartWithPermissionNotGrantedWillCheckPermission() {
        // setup
        whenever(permissionService.permissionGranted()).thenReturn(false)
        // execute
        fixture.onStart()
        // validate
        verify(permissionService).check()
        verify(permissionService).permissionGranted()
    }

    @Test
    fun testOnCreateOptionsMenu() {
        // setup
        val menu: Menu = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // execute
        val actual = fixture.onCreateOptionsMenu(menu)
        // validate
        assertTrue(actual)
        verify(optionMenu).create(fixture, menu)
    }

    @Test
    fun testOnOptionsItemSelected() {
        // setup
        val menuItem: MenuItem = mock()
        val optionMenu: OptionMenu = mock()
        fixture.optionMenu = optionMenu
        // execute
        val actual = fixture.onOptionsItemSelected(menuItem)
        // validate
        assertTrue(actual)
        verify(optionMenu).select(menuItem)
    }

    @Test
    fun testOnConfigurationChanged() {
        // setup
        val configuration = fixture.resources.configuration
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // execute
        fixture.onConfigurationChanged(configuration)
        // validate
        verify(drawerNavigation).onConfigurationChanged(configuration)
    }

    @Test
    fun testOnPostCreate() {
        // setup
        val drawerNavigation: DrawerNavigation = mock()
        fixture.drawerNavigation = drawerNavigation
        // execute
        fixture.onPostCreate(null)
        // validate
        verify(drawerNavigation).syncState()
    }

    @Test
    fun testOnStop() {
        // setup
        val scanner = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onStop()
        // validate
        verify(scanner).stop()
    }

    @Test
    fun testUpdateShouldUpdateScanner() {
        // setup
        val scanner = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.update()
        // validate
        verify(scanner).update()
    }

    @Test
    fun testOnSharedPreferenceChangedShouldUpdateScanner() {
        // setup
        val scanner = MainContextHelper.INSTANCE.scannerService
        // execute
        fixture.onSharedPreferenceChanged(sharedPreferences, String.EMPTY)
        // validate
        verify(scanner).update()
    }

    @Test
    fun testOptionMenu() {
        // execute
        val actual = fixture.optionMenu
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testGetCurrentMenuItem() {
        // setup
        val menuItem: MenuItem = mock()
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentMenuItem()).thenReturn(menuItem)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.currentMenuItem()
        // validate
        assertEquals(menuItem, actual)
        verify(navigationMenuController).currentMenuItem()
    }

    @Test
    fun testGetCurrentNavigationMenu() {
        // setup
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val navigationMenuController: NavigationMenuController = mock()
        whenever(navigationMenuController.currentNavigationMenu()).thenReturn(navigationMenu)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.currentNavigationMenu()
        // validate
        assertEquals(navigationMenu, actual)
        verify(navigationMenuController).currentNavigationMenu()
    }

    @Test
    fun testSetCurrentNavigationMenu() {
        // setup
        val navigationMenu = NavigationMenu.CHANNEL_GRAPH
        val settings = MainContextHelper.INSTANCE.settings
        val navigationMenuController: NavigationMenuController = mock()
        fixture.navigationMenuController = navigationMenuController
        // execute
        fixture.currentNavigationMenu(navigationMenu)
        // validate
        verify(navigationMenuController).currentNavigationMenu(navigationMenu)
        verify(settings).saveSelectedMenu(navigationMenu)
    }

    @Test
    fun testGetNavigationView() {
        // setup
        val navigationMenuController: NavigationMenuController = mock()
        val navigationView: NavigationView = mock()
        whenever(navigationMenuController.navigationView).thenReturn(navigationView)
        fixture.navigationMenuController = navigationMenuController
        // execute
        val actual = fixture.navigationView()
        // validate
        assertEquals(navigationView, actual)
        verify(navigationMenuController).navigationView
    }
}