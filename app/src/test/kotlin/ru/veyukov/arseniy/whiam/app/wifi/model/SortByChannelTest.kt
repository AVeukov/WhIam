
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.util.EMPTY
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.*

class SortByChannelTest {
    private val fixture = sortByChannel()

    @Test
    fun testSortByChannel() {
        // setup
        val wiFiDetail1 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        val wiFiDetail2 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2432, WiFiWidth.MHZ_40, -55, false),
                WiFiAdditional.EMPTY)
        // execute
        val actual: Int = fixture.compare(wiFiDetail1, wiFiDetail2)
        // validate
        assertEquals(0, actual)
    }

    @Test
    fun testSortByChannelWithDifferentChannel() {
        // setup
        val wiFiDetail1 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        val wiFiDetail2 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2432, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual: Int = fixture.compare(wiFiDetail1, wiFiDetail2)
        // validate
        assertEquals(1, actual)
    }

    @Test
    fun testSortByChannelWithDifferentSSID() {
        // setup
        val wiFiDetail1 = WiFiDetail(
                WiFiIdentifier("ssid1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        val wiFiDetail2 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual: Int = fixture.compare(wiFiDetail1, wiFiDetail2)
        // validate
        assertEquals(32, actual)
    }

    @Test
    fun testSortByChannelWithDifferentBSSID() {
        // setup
        val wiFiDetail1 = WiFiDetail(
                WiFiIdentifier("SSID1", "bssid1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        val wiFiDetail2 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual: Int = fixture.compare(wiFiDetail1, wiFiDetail2)
        // validate
        assertEquals(32, actual)
    }

    @Test
    fun testSortByChannelWithDifferentStrength() {
        // setup
        val wiFiDetail1 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
                WiFiAdditional.EMPTY)
        val wiFiDetail2 = WiFiDetail(
                WiFiIdentifier("SSID1", "BSSID1"),
                String.EMPTY,
                WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -35, true),
                WiFiAdditional.EMPTY)
        // execute
        val actual: Int = fixture.compare(wiFiDetail1, wiFiDetail2)
        // validate
        assertEquals(1, actual)
    }
}