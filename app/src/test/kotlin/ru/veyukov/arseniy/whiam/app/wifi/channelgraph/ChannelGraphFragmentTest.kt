
package ru.veyukov.arseniy.whiam.wifi.channelgraph

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.channelgraph.ChannelGraphFragment

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ChannelGraphFragmentTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val scanner = MainContextHelper.INSTANCE.scannerService
    private val fixture = ChannelGraphFragment()

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testOnCreateView() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertNotNull(fixture)
        verify(scanner).update()
        verify(scanner).register(fixture.channelGraphAdapter)
    }

    @Test
    fun testRefreshEnabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.graphRefresh)
        assertTrue(swipeRefreshLayout.isEnabled)
    }

    @Test
    fun testOnResume() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onResume()
        // validate
        verify(scanner, times(2)).update()
        verify(scanner, times(2)).register(fixture.channelGraphAdapter)
    }

    @Test
    fun testOnPause() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onPause()
        // validate
        verify(scanner).unregister(fixture.channelGraphAdapter)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun testRefreshDisabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.graphRefresh)
        assertFalse(swipeRefreshLayout.isRefreshing)
        assertFalse(swipeRefreshLayout.isEnabled)
    }
}