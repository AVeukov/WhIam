
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannel
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.*

class ChannelRatingTest {
    private val wiFiConnection = WiFiConnection(
            WiFiIdentifier("ssid1", "20:CF:30:CE:1D:71"),
            "192.168.1.15",
            11)

    private val wiFiDetail1 = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
            String.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50, true),
            WiFiAdditional(String.EMPTY, wiFiConnection))
    private val wiFiDetail2 = WiFiDetail(
            WiFiIdentifier("SSID2", "58:6d:8f:fa:ae:c0"),
            String.EMPTY,
            WiFiSignal(2442, 2442, WiFiWidth.MHZ_20, -70, true),
            WiFiAdditional.EMPTY)
    private val wiFiDetail3 = WiFiDetail(
            WiFiIdentifier("SSID3", "84:94:8c:9d:40:68"),
            String.EMPTY,
            WiFiSignal(2452, 2452, WiFiWidth.MHZ_20, -60, true),
            WiFiAdditional.EMPTY)
    private val wiFiDetail4 = WiFiDetail(
            WiFiIdentifier("SSID3", "64:A4:8c:90:10:12"),
            String.EMPTY,
            WiFiSignal(2452, 2452, WiFiWidth.MHZ_20, -80, true),
            WiFiAdditional.EMPTY)

    private val fixture = ChannelRating()

    @Test
    fun testChannelRating() {
        // setup
        val wiFiChannel: WiFiChannel = wiFiDetail1.wiFiSignal.centerWiFiChannel
        // execute & validate
        assertEquals(0, fixture.count(wiFiChannel))
        assertEquals(Strength.ZERO, fixture.strength(wiFiChannel))
    }

    @Test
    fun testCount() {
        // setup
        fixture.wiFiDetails(listOf(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4))
        // execute and validate
        validateCount(2, wiFiDetail1.wiFiSignal.centerWiFiChannel)
        validateCount(4, wiFiDetail2.wiFiSignal.centerWiFiChannel)
        validateCount(3, wiFiDetail3.wiFiSignal.centerWiFiChannel)
    }

    private fun validateCount(expected: Int, wiFiChannel: WiFiChannel) {
        assertEquals(expected, fixture.count(wiFiChannel))
    }

    @Test
    fun testStrengthShouldReturnMaximum() {
        // setup
        val other: WiFiDetail = makeCopy(wiFiDetail3)
        fixture.wiFiDetails(listOf(other, wiFiDetail3))
        val expected: Strength = wiFiDetail3.wiFiSignal.strength
        // execute
        val actual: Strength = fixture.strength(wiFiDetail3.wiFiSignal.centerWiFiChannel)
        // execute and validate
        assertEquals(expected, actual)
    }

    @Test
    fun testStrengthWithConnected() {
        // setup
        val other: WiFiDetail = makeCopy(wiFiDetail1)
        fixture.wiFiDetails(listOf(other, wiFiDetail1))
        val expected: Strength = other.wiFiSignal.strength
        // execute
        val actual: Strength = fixture.strength(wiFiDetail1.wiFiSignal.centerWiFiChannel)
        // execute and validate
        assertEquals(expected, actual)
    }

    private fun makeCopy(wiFiDetail: WiFiDetail): WiFiDetail {
        val wiFiSignal: WiFiSignal = wiFiDetail.wiFiSignal
        return WiFiDetail(
                WiFiIdentifier("SSID2-OTHER", "BSSID-OTHER"),
                String.EMPTY,
                WiFiSignal(wiFiSignal.primaryFrequency, wiFiSignal.centerFrequency, wiFiSignal.wiFiWidth, -80, true),
                WiFiAdditional.EMPTY)
    }

    @Test
    fun testBestChannelsSortedInOrderWithMinimumChannels() {
        // setup
        val channels: List<WiFiChannel> = WiFiBand.GHZ2.wiFiChannels.wiFiChannels()
        fixture.wiFiDetails(listOf(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4))
        // execute
        val actual: List<ChannelAPCount> = fixture.bestChannels(channels)
        // validate
        assertEquals(7, actual.size)
        validateChannelAPCount(1, 0, actual[0])
        validateChannelAPCount(2, 0, actual[1])
        validateChannelAPCount(12, 0, actual[2])
        validateChannelAPCount(13, 0, actual[3])
        validateChannelAPCount(14, 0, actual[4])
        validateChannelAPCount(3, 1, actual[5])
        validateChannelAPCount(4, 1, actual[6])
    }

    private fun validateChannelAPCount(expectedChannel: Int, expectedCount: Int, channelAPCount: ChannelAPCount) {
        assertEquals(expectedChannel, channelAPCount.wiFiChannel.channel)
        assertEquals(expectedCount, channelAPCount.count)
    }

    @Test
    fun testSetWiFiChannelsRemovesDuplicateAccessPoints() {
        // setup
        val wiFiDetail = WiFiDetail(
                WiFiIdentifier("SSID2", "22:cf:30:ce:1d:72"),
                String.EMPTY,
                WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, wiFiDetail1.wiFiSignal.level - 5, true),
                WiFiAdditional.EMPTY)
        // execute
        fixture.wiFiDetails(listOf(wiFiDetail1, wiFiDetail))
        // validate
        assertEquals(1, fixture.wiFiDetails().size)
        assertEquals(wiFiDetail1, fixture.wiFiDetails()[0])
    }
}