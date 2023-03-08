
package ru.veyukov.arseniy.whiam.wifi.channelavailable

import android.view.View
import android.widget.TextView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.R
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.channelavailable.ChannelAvailableAdapterBinding

class ChannelAvailableAdapterBindingTest {

    @Test
    fun testChannelAvailableAdapterBinding() {
        // setup
        val view: View = mock()
        val channelAvailableCountry: TextView = mock()
        val channelAvailableTitleGhz2: TextView = mock()
        val channelAvailableGhz2: TextView = mock()
        val channelAvailableTitleGhz5: TextView = mock()
        val channelAvailableGhz5: TextView = mock()
        val channelAvailableTitleGhz6: TextView = mock()
        val channelAvailableGhz6: TextView = mock()
        whenever(view.findViewById<TextView>(R.id.channel_available_country)).thenReturn(channelAvailableCountry)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_2)).thenReturn(channelAvailableTitleGhz2)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_2)).thenReturn(channelAvailableGhz2)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_5)).thenReturn(channelAvailableTitleGhz5)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_5)).thenReturn(channelAvailableGhz5)
        whenever(view.findViewById<TextView>(R.id.channel_available_title_ghz_6)).thenReturn(channelAvailableTitleGhz6)
        whenever(view.findViewById<TextView>(R.id.channel_available_ghz_6)).thenReturn(channelAvailableGhz6)
        // execute
        val fixture = ChannelAvailableAdapterBinding(view)
        // validate
        assertEquals(view, fixture.root)
        assertEquals(channelAvailableCountry, fixture.channelAvailableCountry)
        assertEquals(channelAvailableTitleGhz2, fixture.channelAvailableTitleGhz2)
        assertEquals(channelAvailableGhz2, fixture.channelAvailableGhz2)
        assertEquals(channelAvailableTitleGhz5, fixture.channelAvailableTitleGhz5)
        assertEquals(channelAvailableGhz5, fixture.channelAvailableGhz5)
        assertEquals(channelAvailableTitleGhz6, fixture.channelAvailableTitleGhz6)
        assertEquals(channelAvailableGhz6, fixture.channelAvailableGhz6)
        verify(view).findViewById<TextView>(R.id.channel_available_country)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_2)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_2)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_5)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_5)
        verify(view).findViewById<TextView>(R.id.channel_available_title_ghz_6)
        verify(view).findViewById<TextView>(R.id.channel_available_ghz_6)
    }

}