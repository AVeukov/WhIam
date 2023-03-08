
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import ru.veyukov.arseniy.whiam.R
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointViewType

class AccessPointViewTypeTest {
    @Test
    fun testAccessPointViewCount() {
        assertEquals(2, AccessPointViewType.values().size)
    }

    @Test
    fun testGetLayout() {
        assertEquals(R.layout.access_point_view_complete, AccessPointViewType.COMPLETE.layout)
        assertEquals(R.layout.access_point_view_compact, AccessPointViewType.COMPACT.layout)
    }

}