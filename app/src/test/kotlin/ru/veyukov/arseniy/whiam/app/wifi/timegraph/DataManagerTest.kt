
package ru.veyukov.arseniy.whiam.wifi.timegraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.RobolectricUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.graphutils.*
import ru.veyukov.arseniy.whiam.wifi.model.*
import ru.veyukov.arseniy.whiam.wifi.timegraph.DataManager
import ru.veyukov.arseniy.whiam.wifi.timegraph.TimeGraphCache

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class DataManagerTest {
    private val bssid = "BSSID"
    private val level = -40
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val graphViewWrapper: GraphViewWrapper = mock()
    private val timeGraphCache: TimeGraphCache = mock()
    private val fixture = DataManager(timeGraphCache)

    @Test
    fun testAddSeriesDataIncreaseXValue() {
        // setup
        assertEquals(0, fixture.xValue)
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertEquals(1, fixture.xValue)
    }

    @Test
    fun testAddSeriesDataIncreaseCounts() {
        // setup
        assertEquals(0, fixture.scanCount)
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertEquals(1, fixture.scanCount)
    }

    @Test
    fun testAddSeriesDoesNotIncreasesScanCountWhenLimitIsReached() {
        // setup
        fixture.scanCount = MAX_SCAN_COUNT
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertEquals(MAX_SCAN_COUNT, fixture.scanCount)
    }

    @Test
    fun testAddSeriesSetHorizontalLabelsVisible() {
        // setup
        fixture.scanCount = 1
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertEquals(2, fixture.scanCount)
        verify(graphViewWrapper).setHorizontalLabelsVisible(true)
    }

    @Test
    fun testAddSeriesDoesNotSetHorizontalLabelsVisible() {
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        verify(graphViewWrapper, never()).setHorizontalLabelsVisible(true)
    }

    @Test
    fun testAdjustDataAppendsData() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = setOf()
        val difference = makeWiFiDetails()
        val xValue = fixture.xValue
        val scanCount = fixture.scanCount
        val dataPoint = GraphDataPoint(xValue, MIN_Y + MIN_Y_OFFSET)
        whenever(graphViewWrapper.differenceSeries(wiFiDetails)).thenReturn(difference)
        // execute
        fixture.adjustData(graphViewWrapper, wiFiDetails)
        // validate
        difference.forEach {
            verify(graphViewWrapper).appendToSeries(
                    it,
                    dataPoint,
                    scanCount,
                    it.wiFiAdditional.wiFiConnection.connected)
            verify(timeGraphCache).add(it)
        }
        verify(timeGraphCache).clear()
    }

    @Test
    fun testNewSeries() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = makeWiFiDetails().toSet()
        val moreWiFiDetails: Set<WiFiDetail> = makeMoreWiFiDetails().toSet()
        whenever(timeGraphCache.active()).thenReturn(moreWiFiDetails)
        // execute
        val actual = fixture.newSeries(wiFiDetails)
        // validate
        assertTrue(actual.containsAll(wiFiDetails))
        assertTrue(actual.containsAll(moreWiFiDetails))
        verify(timeGraphCache).active()
    }

    @Test
    fun testAddDataToExistingSeries() {
        // setup
        val scanCount = fixture.scanCount
        val xValue = fixture.xValue
        val wiFiDetail = makeWiFiDetail("SSID")
        val dataPoint = GraphDataPoint(xValue, level)
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addData(graphViewWrapper, wiFiDetail, MAX_Y)
        // validate
        verify(graphViewWrapper).newSeries(wiFiDetail)
        verify(graphViewWrapper).appendToSeries(
                wiFiDetail,
                dataPoint,
                scanCount,
                wiFiDetail.wiFiAdditional.wiFiConnection.connected)
        verify(timeGraphCache).reset(wiFiDetail)
    }

    @Test
    fun testAddDataToExistingSeriesExpectLevelToEqualToLevelMax() {
        // setup
        val expectedLevel = level - 10
        val scanCount = fixture.scanCount
        val xValue = fixture.xValue
        val wiFiDetail = makeWiFiDetail("SSID")
        val dataPoint = GraphDataPoint(xValue, expectedLevel)
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addData(graphViewWrapper, wiFiDetail, expectedLevel)
        // validate
        verify(graphViewWrapper).appendToSeries(
                wiFiDetail,
                dataPoint,
                scanCount,
                wiFiDetail.wiFiAdditional.wiFiConnection.connected)
    }

    @Test
    fun testAddDataNewSeries() {
        // setup
        val wiFiDetail = makeWiFiDetailConnected("SSID")
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(true)
        // execute
        fixture.addData(graphViewWrapper, wiFiDetail, MAX_Y)
        // validate
        verify(graphViewWrapper).newSeries(wiFiDetail)
        verify(timeGraphCache).reset(wiFiDetail)
        verify(graphViewWrapper).addSeries(
                eq(wiFiDetail),
                any(),
                eq(wiFiDetail.wiFiAdditional.wiFiConnection.connected))
    }

    private fun makeWiFiDetailConnected(SSID: String): WiFiDetail {
        val wiFiIdentifier = WiFiIdentifier(SSID, bssid)
        val wiFiConnection = WiFiConnection(wiFiIdentifier, "IPADDRESS", 11)
        val wiFiAdditional = WiFiAdditional("VendorName", wiFiConnection)
        return WiFiDetail(wiFiIdentifier, String.EMPTY, makeWiFiSignal(), wiFiAdditional)
    }

    private fun makeWiFiSignal(): WiFiSignal =
            WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, level, true)

    private fun makeWiFiDetail(SSID: String): WiFiDetail =
            WiFiDetail(WiFiIdentifier(SSID, bssid), String.EMPTY, makeWiFiSignal(), WiFiAdditional.EMPTY)

    private fun makeWiFiDetails(): List<WiFiDetail> =
            listOf(makeWiFiDetailConnected("SSID1"), makeWiFiDetail("SSID2"), makeWiFiDetail("SSID3"))

    private fun makeMoreWiFiDetails(): List<WiFiDetail> =
            listOf(makeWiFiDetail("SSID4"), makeWiFiDetail("SSID5"))

}
