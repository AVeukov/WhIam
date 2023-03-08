
package ru.veyukov.arseniy.whiam.wifi.channelrating

import android.os.Build
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContextHelper.INSTANCE
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.channelrating.ChannelRatingFragment

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ChannelRatingFragmentTest {
    private val mainActivity: MainActivity = RobolectricUtil.INSTANCE.activity
    private val scanner: ScannerService = INSTANCE.scannerService
    private val fixture: ChannelRatingFragment = ChannelRatingFragment()

    @After
    fun tearDown() {
        INSTANCE.restore()
    }

    @Test
    fun testOnCreateView() {
        // execute
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        verify(scanner).update()
        verify(scanner).register(fixture.channelRatingAdapter)
    }

    @Test
    fun testRefreshEnabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.channelRatingRefresh)
        // validate
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
        verify(scanner, times(2)).register(fixture.channelRatingAdapter)
    }

    @Test
    fun testOnPause() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        fixture.onPause()
        // validate
        verify(scanner).unregister(fixture.channelRatingAdapter)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun testRefreshDisabled() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // execute
        val swipeRefreshLayout: SwipeRefreshLayout = fixture.view!!.findViewById(R.id.channelRatingRefresh)
        // validate
        assertFalse(swipeRefreshLayout.isRefreshing)
        assertFalse(swipeRefreshLayout.isEnabled)
    }
}