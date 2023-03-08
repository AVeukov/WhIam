
package ru.veyukov.arseniy.whiam.navigation.items

import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.navigation.items.FragmentItem

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class FragmentItemTest {
    private val title = "title"
    private val fragment: Fragment = mock()
    private val mainActivity: MainActivity = mock()
    private val menuItem: MenuItem = mock()
    private val fragmentManager: FragmentManager = mock()
    private val fragmentTransaction: FragmentTransaction = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(fragment)
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(fragmentManager)
        verifyNoMoreInteractions(fragmentTransaction)
    }

    @Test
    fun testActivateWithStateSaved() {
        // setup
        val fixture = FragmentItem(fragment, true, View.VISIBLE)
        val navigationMenu = NavigationMenu.ACCESS_POINTS
        whenever(mainActivity.supportFragmentManager).thenReturn(fragmentManager)
        whenever(fragmentManager.isStateSaved).thenReturn(true)
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu)
        // validate
        verify(mainActivity).supportFragmentManager
        verify(fragmentManager).isStateSaved
        verifyFragmentManagerIsNotCalled()
        verifyNoChangesToMainActivity(navigationMenu)
    }

    @Test
    fun testActivateWithStateNotSaved() {
        // setup
        val fixture = FragmentItem(fragment, true, View.VISIBLE)
        val navigationMenu = NavigationMenu.ACCESS_POINTS
        whenever(mainActivity.supportFragmentManager).thenReturn(fragmentManager)
        whenever(fragmentManager.isStateSaved).thenReturn(false)
        whenever(menuItem.title).thenReturn(title)
        withFragmentTransaction()
        // execute
        fixture.activate(mainActivity, menuItem, navigationMenu)
        // validate
        verify(mainActivity).supportFragmentManager
        verify(fragmentManager).isStateSaved
        verify(menuItem).title
        verifyFragmentManager()
        verifyMainActivityChanges(navigationMenu)
    }

    @Test
    fun testRegisteredFalse() {
        // setup
        val fixture = FragmentItem(fragment, false, View.VISIBLE)
        // execute & validate
        assertFalse(fixture.registered)
    }

    @Test
    fun testRegisteredTrue() {
        // setup
        val fixture = FragmentItem(fragment, true, View.VISIBLE)
        // execute & validate
        assertTrue(fixture.registered)
    }

    @Test
    fun testVisibility() {
        // setup
        val fixture = FragmentItem(fragment, false, View.INVISIBLE)
        // execute & validate
        assertEquals(View.INVISIBLE, fixture.visibility)
    }

    private fun withFragmentTransaction() {
        whenever(fragmentManager.beginTransaction()).thenReturn(fragmentTransaction)
        whenever(fragmentTransaction.replace(R.id.main_fragment, fragment)).thenReturn(fragmentTransaction)
    }

    private fun verifyFragmentManager() {
        verify(fragmentManager).beginTransaction()
        verify(fragmentTransaction).replace(R.id.main_fragment, fragment)
        verify(fragmentTransaction).commit()
    }

    private fun verifyMainActivityChanges(navigationMenu: NavigationMenu) {
        verify(mainActivity).currentNavigationMenu(navigationMenu)
        verify(mainActivity).title = title
        verify(mainActivity).updateActionBar()
        verify(mainActivity).mainConnectionVisibility(View.VISIBLE)
    }

    private fun verifyFragmentManagerIsNotCalled() {
        verify(fragmentManager, never()).beginTransaction()
        verify(fragmentTransaction, never()).replace(R.id.main_fragment, fragment)
        verify(fragmentTransaction, never()).commit()
    }

    private fun verifyNoChangesToMainActivity(navigationMenu: NavigationMenu) {
        verify(mainActivity, never()).currentNavigationMenu(navigationMenu)
        verify(mainActivity, never()).title = title
        verify(mainActivity, never()).updateActionBar()
    }
}