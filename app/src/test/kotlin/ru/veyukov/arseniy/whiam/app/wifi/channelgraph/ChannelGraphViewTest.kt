
package ru.veyukov.arseniy.whiam.wifi.channelgraph

import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jjoe64.graphview.GraphView
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.Configuration
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.settings.ThemeStyle
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannel
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannelPair
import ru.veyukov.arseniy.whiam.wifi.graphutils.GraphLegend
import ru.veyukov.arseniy.whiam.wifi.graphutils.GraphViewWrapper
import ru.veyukov.arseniy.whiam.wifi.graphutils.MAX_Y
import ru.veyukov.arseniy.whiam.wifi.model.SortBy
import ru.veyukov.arseniy.whiam.wifi.model.WiFiConnection
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.predicate.Predicate
import ru.veyukov.arseniy.whiam.wifi.predicate.truePredicate
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.channelgraph.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ChannelGraphViewTest {
    private val wiFiChannelPair: WiFiChannelPair = WiFiChannelPair(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN)
    private val settings: Settings = MainContextHelper.INSTANCE.settings
    private val configuration: Configuration = MainContextHelper.INSTANCE.configuration
    private val graphViewWrapper: GraphViewWrapper = mock()
    private val dataManager: DataManager = mock()
    private val fixture: ChannelGraphView = spy(ChannelGraphView(WiFiBand.GHZ2, wiFiChannelPair, dataManager, graphViewWrapper))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(graphViewWrapper)
        verifyNoMoreInteractions(dataManager)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testUpdate() {
        // setup
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiDetails: List<WiFiDetail> = listOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        doReturn(true).whenever(fixture).selected()
        whenever(dataManager.newSeries(wiFiDetails, wiFiChannelPair)).thenReturn(newSeries)
        whenever(settings.channelGraphLegend()).thenReturn(GraphLegend.RIGHT)
        whenever(settings.graphMaximumY()).thenReturn(MAX_Y)
        whenever(settings.themeStyle()).thenReturn(ThemeStyle.DARK)
        whenever(settings.sortBy()).thenReturn(SortBy.CHANNEL)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(fixture).selected()
        verify(fixture).predicate(settings)
        verify(dataManager).newSeries(wiFiDetails, wiFiChannelPair)
        verify(dataManager).addSeriesData(graphViewWrapper, newSeries, MAX_Y)
        verify(graphViewWrapper).removeSeries(newSeries)
        verify(graphViewWrapper).updateLegend(GraphLegend.RIGHT)
        verify(graphViewWrapper).visibility(View.VISIBLE)
        verify(settings).sortBy()
        verify(settings).channelGraphLegend()
        verify(settings).graphMaximumY()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun testGraphView() {
        // setup
        val expected: GraphView = mock()
        whenever(graphViewWrapper.graphView).thenReturn(expected)
        // execute
        val actual = fixture.graphView()
        // validate
        assertEquals(expected, actual)
        verify(graphViewWrapper).graphView
    }

    @Test
    fun testWiFiChannelPairNumX() {
        // setup
        val expected = 15
        val wiFiChannelPair: WiFiChannelPair = withWiFiChannelPair()
        // execute
        val actual = wiFiChannelPair.numX()
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testWiFiChannelPairSelected() {
        // setup
        val fixture = withWiFiChannelPair()
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ2)).thenReturn(fixture)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ2)
        // validate
        assertTrue(actual)
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ2)
    }

    @Test
    fun testWiFiChannelPairSelectedWithCurrentWiFiBandGHZ5() {
        // setup
        val fixture = withWiFiChannelPair()
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ5)).thenReturn(fixture)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ2)
        // validate
        assertFalse(actual)
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5)
    }

    @Test
    fun testWiFiChannelPairSelectedWithGHZ5() {
        // setup
        val fixture = withWiFiChannelPair()
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ2)).thenReturn(fixture)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ5)
        // validate
        assertFalse(actual)
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ2)
    }

    @Test
    fun testWiFiChannelPairSelectedWithCurrentGHZ5() {
        // setup
        val fixture = withWiFiChannelPair()
        val wiFiChannelPair = withWiFiChannelPair(30)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ5)).thenReturn(wiFiChannelPair)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ5)
        // validate
        assertFalse(actual)
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5)
    }

    @Test
    fun testMakeGraphView() {
        // setup
        RobolectricUtil.INSTANCE.activity
        val wiFiChannelPair = withWiFiChannelPair()
        // execute
        val actual = makeGraphView(MainContext.INSTANCE, 10, ThemeStyle.DARK, WiFiBand.GHZ2, wiFiChannelPair)
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testMakeGraphViewWrapper() {
        // setup
        MainContextHelper.INSTANCE.restore()
        RobolectricUtil.INSTANCE.activity
        val wiFiChannelPair = withWiFiChannelPair()
        // execute
        val actual = makeGraphViewWrapper(WiFiBand.GHZ2, wiFiChannelPair)
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testMakeDefaultSeries() {
        // setup
        val frequencyEnd = 10
        val minX = 20
        RobolectricUtil.INSTANCE.activity
        // execute
        val actual = makeDefaultSeries(frequencyEnd, minX)
        // validate
        assertNotNull(actual)
    }

    private fun withWiFiChannelPair(channel: Int = 10): WiFiChannelPair =
        WiFiChannelPair(WiFiChannel(channel, 100), WiFiChannel(20, 200))

}