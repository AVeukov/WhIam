
package ru.veyukov.arseniy.whiam.wifi.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.SortBy
import ru.veyukov.arseniy.whiam.wifi.model.sortByChannel
import ru.veyukov.arseniy.whiam.wifi.model.sortBySSID
import ru.veyukov.arseniy.whiam.wifi.model.sortByStrength

class SortByTest {
    @Test
    fun testSortByNumber() {
        assertEquals(3, SortBy.values().size)
    }

    @Test
    fun testComparator() {
        assertTrue(SortBy.STRENGTH.sort.javaClass.isInstance(sortByStrength()))
        assertTrue(SortBy.SSID.sort.javaClass.isInstance(sortBySSID()))
        assertTrue(SortBy.CHANNEL.sort.javaClass.isInstance(sortByChannel()))
    }
}