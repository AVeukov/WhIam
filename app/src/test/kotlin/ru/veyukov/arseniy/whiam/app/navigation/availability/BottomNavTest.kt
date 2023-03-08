
package ru.veyukov.arseniy.whiam.navigation.availability

import android.view.View
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import org.junit.After
import org.junit.Test
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionBottomNavOff
import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionBottomNavOn

class BottomNavTest {
    private val mainActivity: MainActivity = mock()
    private val view: View = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun testNavigationOptionBottomNavOff() {
        // setup
        whenever<View>(mainActivity.findViewById(R.id.nav_bottom)).thenReturn(view)
        // execute
        navigationOptionBottomNavOff(mainActivity)
        // validate
        verify(mainActivity).findViewById<View>(R.id.nav_bottom)
        verify(view).visibility = View.GONE
    }

    @Test
    fun testNavigationOptionBottomNavOn() {
        // setup
        whenever<View>(mainActivity.findViewById(R.id.nav_bottom)).thenReturn(view)
        // execute
        navigationOptionBottomNavOn(mainActivity)
        // validate
        verify(mainActivity).findViewById<View>(R.id.nav_bottom)
        verify(view).visibility = View.VISIBLE
    }

}

