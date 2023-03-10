
package ru.veyukov.arseniy.whiam.navigation.items

import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.veyukov.arseniy.whiam.about.AboutFragment
import ru.veyukov.arseniy.whiam.settings.SettingsFragment
import ru.veyukov.arseniy.whiam.vendor.VendorFragment
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointsFragment
import ru.veyukov.arseniy.whiam.wifi.channelavailable.ChannelAvailableFragment
import ru.veyukov.arseniy.whiam.wifi.channelgraph.ChannelGraphFragment
import ru.veyukov.arseniy.whiam.wifi.channelrating.ChannelRatingFragment
import ru.veyukov.arseniy.whiam.wifi.timegraph.TimeGraphFragment
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.navigation.items.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class NavigationItemsTest {
    @Test
    fun testFragmentItem() {
        assertTrue((navigationItemAccessPoints as FragmentItem).fragment is AccessPointsFragment)
        assertTrue((navigationItemChannelRating as FragmentItem).fragment is ChannelRatingFragment)
        assertTrue((navigationItemChannelGraph as FragmentItem).fragment is ChannelGraphFragment)
        assertTrue((navigationItemTimeGraph as FragmentItem).fragment is TimeGraphFragment)
        assertTrue((navigationItemChannelAvailable as FragmentItem).fragment is ChannelAvailableFragment)
        assertTrue((navigationItemVendors as FragmentItem).fragment is VendorFragment)
        assertTrue((navigationItemSettings as FragmentItem).fragment is SettingsFragment)
        assertTrue((navigationItemAbout as FragmentItem).fragment is AboutFragment)
    }

    @Test
    fun testRegisteredTrue() {
        assertTrue(navigationItemAccessPoints.registered)
        assertTrue(navigationItemChannelRating.registered)
        assertTrue(navigationItemChannelGraph.registered)
        assertTrue(navigationItemTimeGraph.registered)
    }

    @Test
    fun testRegisteredFalse() {
        assertFalse(navigationItemExport.registered)
        assertFalse(navigationItemChannelAvailable.registered)
        assertFalse(navigationItemVendors.registered)
        assertFalse(navigationItemSettings.registered)
        assertFalse(navigationItemAbout.registered)
    }

    @Test
    fun testVisibility() {
        assertEquals(View.VISIBLE, navigationItemAccessPoints.visibility)
        assertEquals(View.VISIBLE, navigationItemChannelRating.visibility)
        assertEquals(View.VISIBLE, navigationItemChannelGraph.visibility)
        assertEquals(View.VISIBLE, navigationItemTimeGraph.visibility)
        assertEquals(View.VISIBLE, navigationItemChannelAvailable.visibility)
        assertEquals(View.GONE, navigationItemVendors.visibility)
        assertEquals(View.GONE, navigationItemExport.visibility)
        assertEquals(View.GONE, navigationItemSettings.visibility)
        assertEquals(View.GONE, navigationItemAbout.visibility)
    }

    @Test
    fun testExportItem() {
        assertTrue(navigationItemExport is ExportItem)
    }
}