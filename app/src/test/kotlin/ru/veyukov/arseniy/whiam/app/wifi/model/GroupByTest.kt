
package ru.veyukov.arseniy.whiam.wifi.model

import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.*

class GroupByTest {

    @Test
    fun testGroupByNumber() {
        assertEquals(4, GroupBy.values().size)
    }

    @Test
    fun testGroupBySort() {
        assertTrue(GroupBy.CHANNEL.sort.javaClass.isInstance(sortByChannel()))
        assertTrue(GroupBy.NONE.sort.javaClass.isInstance(sortByDefault()))
        assertTrue(GroupBy.SSID.sort.javaClass.isInstance(sortBySSID()))
        assertTrue(GroupBy.VIRTUAL.sort.javaClass.isInstance(sortBySSID()))
    }

    @Test
    fun testGroupByGroup() {
        assertTrue(GroupBy.CHANNEL.group.javaClass.isInstance(groupByChannel))
        assertTrue(GroupBy.NONE.group.javaClass.isInstance(groupBySSID))
        assertTrue(GroupBy.SSID.group.javaClass.isInstance(groupBySSID))
        assertTrue(GroupBy.VIRTUAL.group.javaClass.isInstance(groupByVirtual))
    }

    @Test
    fun testNone() {
        assertFalse(GroupBy.CHANNEL.none)
        assertTrue(GroupBy.NONE.none)
        assertFalse(GroupBy.SSID.none)
        assertFalse(GroupBy.VIRTUAL.none)
    }

    @Test
    fun testGroupByKeyWithNone() {
        // setup
        val expected = "SSID_TO_TEST"
        val wiFiDetail = WiFiDetail(WiFiIdentifier(expected))
        // execute
        val actual: String = GroupBy.NONE.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGroupByKeyWithSSID() {
        // setup
        val expected = "SSID_TO_TEST"
        val wiFiDetail = WiFiDetail(WiFiIdentifier(expected))
        // execute
        val actual: String = GroupBy.SSID.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGroupByKeyWithChannel() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val expected = "2435"
        // execute
        val actual: String = GroupBy.CHANNEL.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGroupByKeyWithVirtual() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val expected = ":cf:30:ce:1d:7-2435"
        // execute
        val actual: String = GroupBy.VIRTUAL.group(wiFiDetail)
        // validate
        assertEquals(expected, actual)
    }

    private fun withWiFiDetail() = WiFiDetail(
        WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
        "WPA-WPA2",
        WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -40, true)
    )
}