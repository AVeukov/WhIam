
package ru.veyukov.arseniy.whiam.wifi.channelgraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.model.WiFiConnection
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.channelgraph.ChannelGraphAdapter
import ru.veyukov.arseniy.whiam.wifi.channelgraph.ChannelGraphNavigation

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ChannelGraphAdapterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val channelGraphNavigation: ChannelGraphNavigation = mock()
    private val fixture = ChannelGraphAdapter(channelGraphNavigation)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(channelGraphNavigation)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testGetGraphViewNotifiers() {
        // setup
        val expected = WiFiBand.values().sumOf { it.wiFiChannels.wiFiChannelPairs().size }
        // execute
        val graphViewNotifiers = fixture.graphViewNotifiers()
        // validate
        assertEquals(expected, graphViewNotifiers.size)
    }

    @Test
    fun testGetGraphViews() {
        // setup
        val expected = WiFiBand.values().sumOf { it.wiFiChannels.wiFiChannelPairs().size }
        // execute
        val graphViews = fixture.graphViews()
        // validate
        assertEquals(expected, graphViews.size)
    }

    @Test
    fun testUpdate() {
        // setup
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(channelGraphNavigation).update()
    }
}