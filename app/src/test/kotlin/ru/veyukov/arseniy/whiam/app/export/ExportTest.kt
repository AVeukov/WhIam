
package ru.veyukov.arseniy.whiam.export

import android.content.Intent
import android.content.res.Resources
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.model.WiFiIdentifier
import ru.veyukov.arseniy.whiam.wifi.model.WiFiSignal
import ru.veyukov.arseniy.whiam.wifi.model.WiFiWidth
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.export.Export
import ru.veyukov.arseniy.whiam.export.ExportIntent
import java.text.SimpleDateFormat
import java.util.*

class ExportTest {
    private val name = "name"
    private val date = Date()

    private val mainActivity: MainActivity = mock()
    private val exportIntent: ExportIntent = mock()
    private val intent: Intent = mock()
    private val resources: Resources = mock()
    private val fixture = Export(exportIntent)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(exportIntent)
        verifyNoMoreInteractions(intent)
        verifyNoMoreInteractions(resources)
    }

    @Test
    fun testExport() {
        // setup
        val wiFiDetails = withWiFiDetails()
        val timestamp = timestamp(date)
        val title = title(timestamp)
        val data = data(timestamp)
        whenever(mainActivity.resources).thenReturn(resources)
        whenever(resources.getString(R.string.action_access_points)).thenReturn(name)
        whenever(exportIntent.intent(title, data)).thenReturn(intent)
        // execute
        val actual = fixture.export(mainActivity, wiFiDetails, date)
        // validate
        assertEquals(intent, actual)
        verify(mainActivity).resources
        verify(resources).getString(R.string.action_access_points)
        verify(exportIntent).intent(title, data)
    }

    @Test
    fun testTimestamp() {
        // setup
        val expected = timestamp(date)
        // execute
        val actual = fixture.timestamp(date)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testData() {
        // setup
        val wiFiDetails = withWiFiDetails()
        val timestamp = timestamp(date)
        val expected = data(timestamp)
        // execute
        val actual = fixture.data(wiFiDetails, timestamp)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testTitle() {
        // setup
        val timestamp = timestamp(date)
        val expected = "$name-$timestamp"
        whenever(mainActivity.resources).thenReturn(resources)
        whenever(resources.getString(R.string.action_access_points)).thenReturn(name)
        // execute
        val actual = fixture.title(mainActivity, timestamp)
        // validate
        assertEquals(expected, actual)
        verify(mainActivity).resources
        verify(resources).getString(R.string.action_access_points)
    }

    private fun title(timestamp: String): String = "$name-$timestamp"

    private fun data(timestamp: String): String =
        "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|Timestamp|802.11mc|Security\n" +
                timestamp + "|SSID10|BSSID10|-10dBm|3|2422MHz|5|2432MHz|40MHz (2412 - 2452)|~0.0m|0|true|capabilities10\n" +
                timestamp + "|SSID20|BSSID20|-20dBm|5|2432MHz|7|2442MHz|40MHz (2422 - 2462)|~0.1m|0|true|capabilities20\n" +
                timestamp + "|SSID30|BSSID30|-30dBm|7|2442MHz|9|2452MHz|40MHz (2432 - 2472)|~0.3m|0|true|capabilities30\n"

    private fun timestamp(date: Date): String = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.US).format(date)

    private fun withWiFiDetails(): List<WiFiDetail> =
            listOf(withWiFiDetail(10), withWiFiDetail(20), withWiFiDetail(30))

    private fun withWiFiDetail(offset: Int): WiFiDetail {
        val wiFiSignal = WiFiSignal(2412 + offset, 2422 + offset, WiFiWidth.MHZ_40, -offset, true)
        val wiFiIdentifier = WiFiIdentifier("SSID$offset", "BSSID$offset")
        return WiFiDetail(wiFiIdentifier, "capabilities$offset", wiFiSignal)
    }

}