
package ru.veyukov.arseniy.whiam.wifi.channelavailable

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.MainContextHelper.INSTANCE
import ru.veyukov.arseniy.whiam.RobolectricUtil
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.channelavailable.ChannelAvailableFragment
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ChannelAvailableFragmentTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = INSTANCE.settings
    private val fixture = ChannelAvailableFragment()

    @Before
    fun setUp() {
        whenever(settings.countryCode()).thenReturn(Locale.US.country)
    }

    @After
    fun tearDown() {
        verify(settings, atLeastOnce()).countryCode()
        INSTANCE.restore()
    }

    @Test
    fun testOnCreateView() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertNotNull(fixture)
    }
}