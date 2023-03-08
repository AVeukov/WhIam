
package ru.veyukov.arseniy.whiam.navigation.availability

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContextHelper.INSTANCE
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionFilterOn

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class FilterOnTest {
    private val mainActivity: MainActivity = mock()
    private val optionMenu: OptionMenu = mock()
    private val menu: Menu = mock()
    private val menuItem: MenuItem = mock()
    private val drawable: Drawable = mock()
    private val filterAdapter = INSTANCE.filterAdapter

    @After
    fun tearDown() {
        INSTANCE.restore()
        verifyNoMoreInteractions(filterAdapter)
        verifyNoMoreInteractions(drawable)
        verifyNoMoreInteractions(menuItem)
        verifyNoMoreInteractions(menu)
        verifyNoMoreInteractions(optionMenu)
        verifyNoMoreInteractions(mainActivity)
    }

    @Test
    fun testNavigationOptionFilterOnWithFilterInactive() {
        // setup
        val colorResult = 200
        whenever(filterAdapter.isActive()).thenReturn(false)
        whenever(ContextCompat.getColor(mainActivity, R.color.regular)).thenReturn(colorResult)
        withMenuItem()
        // execute
        navigationOptionFilterOn(mainActivity)
        // validate
        verifyMenuItem()
        ContextCompat.getColor(verify(mainActivity), R.color.regular)
        verify(drawable).setTint(colorResult)
    }

    @Test
    fun testNavigationOptionFilterOnWithFilterActive() {
        // setup
        val colorResult = 100
        whenever(filterAdapter.isActive()).thenReturn(true)
        whenever(ContextCompat.getColor(mainActivity, R.color.selected)).thenReturn(colorResult)
        withMenuItem()
        // execute
        navigationOptionFilterOn(mainActivity)
        // validate
        verifyMenuItem()
        ContextCompat.getColor(verify(mainActivity), R.color.selected)
        verify(drawable).setTint(colorResult)
    }

    @Test
    fun testNavigationOptionFilterOnWithNoMenuDoesNotSetVisibleTrue() {
        // setup
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(null)
        // execute
        navigationOptionFilterOn(mainActivity)
        // validate
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
    }

    private fun verifyMenuItem() {
        verify(mainActivity).optionMenu
        verify(optionMenu).menu
        verify(menu).findItem(R.id.action_filter)
        verify(menuItem).icon
        verify(filterAdapter).isActive()
        verify(menuItem).isVisible = true
    }

    private fun withMenuItem() {
        whenever(mainActivity.optionMenu).thenReturn(optionMenu)
        whenever(optionMenu.menu).thenReturn(menu)
        whenever(menu.findItem(R.id.action_filter)).thenReturn(menuItem)
        whenever(menuItem.icon).thenReturn(drawable)
    }
}