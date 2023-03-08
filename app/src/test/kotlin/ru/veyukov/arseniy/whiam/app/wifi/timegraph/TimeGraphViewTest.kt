
package ru.veyukov.arseniy.whiam.wifi.timegraph

import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jjoe64.graphview.GraphView
import com.nhaarman.mockitokotlin2.*
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.settings.ThemeStyle
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.timegraph.DataManager
import ru.veyukov.arseniy.whiam.wifi.timegraph.TimeGraphView
import ru.veyukov.arseniy.whiam.wifi.timegraph.makeGraphView
import ru.veyukov.arseniy.whiam.wifi.timegraph.makeGraphViewWrapper

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class TimeGraphViewTest {
    private val dataManager: DataManager = mock()
    private val graphViewWrapper: GraphViewWrapper = mock()
    private val fixture: TimeGraphView = spy(TimeGraphView(WiFiBand.GHZ2, dataManager, graphViewWrapper))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(dataManager)
        verifyNoMoreInteractions(graphViewWrapper)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testUpdate() {
        // setup
        val settings = MainContextHelper.INSTANCE.settings
        val wiFiDetails: List<WiFiDetail> = listOf()
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        whenever(dataManager.addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)).thenReturn(newSeries)
        whenever(settings.sortBy()).thenReturn(SortBy.SSID)
        whenever(settings.timeGraphLegend()).thenReturn(GraphLegend.LEFT)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.graphMaximumY()).thenReturn(MAX_Y)
        whenever(settings.themeStyle()).thenReturn(ThemeStyle.DARK)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(fixture).predicate(settings)
        verify(dataManager).addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)
        verify(graphViewWrapper).removeSeries(newSeries)
        verify(graphViewWrapper).updateLegend(GraphLegend.LEFT)
        verify(graphViewWrapper).visibility(View.VISIBLE)
        verify(settings).sortBy()
        verify(settings).timeGraphLegend()
        verify(settings).graphMaximumY()
        verify(settings).wiFiBand()
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
        verifyNoMoreInteractions(expected)
    }

    @Test
    fun testMakeGraphView() {
        // setup
        RobolectricUtil.INSTANCE.activity
        // execute
        val actual = makeGraphView(MainContext.INSTANCE, 10, ThemeStyle.DARK)
        // validate
        assertNotNull(actual)
    }

    @Test
    fun testMakeGraphViewWrapper() {
        // setup
        RobolectricUtil.INSTANCE.activity
        // execute
        val actual = makeGraphViewWrapper()
        // validate
        assertNotNull(actual)
    }
}