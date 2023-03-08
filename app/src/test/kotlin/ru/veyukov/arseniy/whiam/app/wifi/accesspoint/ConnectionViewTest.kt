
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.net.wifi.WifiInfo
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.MainContextHelper
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.model.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ConnectionViewTest {
    private val ssid = "SSID"
    private val bssid = "BSSID"
    private val ipAddress = "IP-ADDRESS"
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val settings = MainContextHelper.INSTANCE.settings
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper
    private val wiFiData: WiFiData = mock()
    private val accessPointDetail: AccessPointDetail = mock()
    private val accessPointPopup: AccessPointPopup = mock()
    private val fixture = ConnectionView(mainActivity, accessPointDetail, accessPointPopup)

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        mainActivity.currentNavigationMenu(NavigationMenu.ACCESS_POINTS)
    }

    @Test
    fun testConnectionGoneWithNoConnectionInformation() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.connection).visibility)
        verifyConnectionInformation()
    }

    @Test
    fun testConnectionGoneWithConnectionInformationAndHideType() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.HIDE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.connection).visibility)
        verifyConnectionInformation()
    }

    @Test
    fun testConnectionVisibleWithConnectionInformation() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById<View>(R.id.connection).visibility)
        verifyConnectionInformation()
    }

    @Test
    fun testConnectionWithConnectionInformation() {
        // setup
        val wiFiAdditional = withWiFiAdditional()
        val connection = withConnection(wiFiAdditional)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        val wiFiConnection = wiFiAdditional.wiFiConnection
        val view = mainActivity.findViewById<View>(R.id.connection)
        val ipAddressView = view.findViewById<TextView>(R.id.ipAddress)
        assertEquals(wiFiConnection.ipAddress, ipAddressView.text.toString())
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertEquals(View.VISIBLE, linkSpeedView.visibility)
        assertEquals(wiFiConnection.linkSpeed.toString() + WifiInfo.LINK_SPEED_UNITS, linkSpeedView.text.toString())
    }

    @Test
    fun testConnectionWithInvalidLinkSpeed() {
        // setup
        val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
        val wiFiConnection = WiFiConnection(wiFiIdentifier, ipAddress, WiFiConnection.LINK_SPEED_INVALID)
        val connection = withConnection(WiFiAdditional(String.EMPTY, wiFiConnection))
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(connection)
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        val view = mainActivity.findViewById<View>(R.id.connection)
        val linkSpeedView = view.findViewById<TextView>(R.id.linkSpeed)
        assertEquals(View.GONE, linkSpeedView.visibility)
    }

    @Test
    fun testNoDataIsVisibleWithNoWiFiDetails() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(wiFiData.connection()).thenReturn(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById<View>(R.id.no_data).visibility)
        assertEquals(View.VISIBLE, mainActivity.findViewById<View>(R.id.no_location).visibility)
        assertEquals(View.VISIBLE, mainActivity.findViewById<View>(R.id.throttling).visibility)
        verify(wiFiData).wiFiDetails
    }

    @Test
    fun testNoDataIsGoneWithWiFiDetails() {
        // setup
        val wiFiDetail = withConnection(WiFiAdditional.EMPTY)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(wiFiData.connection()).thenReturn(wiFiDetail)
        whenever(wiFiData.wiFiDetails).thenReturn(listOf(wiFiDetail))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.no_data).visibility)
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.no_location).visibility)
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.throttling).visibility)
        verify(wiFiData).wiFiDetails
    }

    @Test
    fun testNoDataIsGoneWithNavigationMenuThatDoesNotHaveOptionMenu() {
        // setup
        mainActivity.currentNavigationMenu(NavigationMenu.VENDORS)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(wiFiData.connection()).thenReturn(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.no_data).visibility)
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.no_location).visibility)
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.throttling).visibility)
        verify(wiFiData, never()).wiFiDetails
    }

    @Test
    fun testScanningIsVisibleWithNoWiFiDetails() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(wiFiData.connection()).thenReturn(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById<View>(R.id.scanning).visibility)
        verify(wiFiData).wiFiDetails
    }

    @Test
    fun testScanningIsGoneWithWiFiDetails() {
        // setup
        val wiFiDetail = withConnection(WiFiAdditional.EMPTY)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(wiFiData.connection()).thenReturn(wiFiDetail)
        whenever(wiFiData.wiFiDetails).thenReturn(listOf(wiFiDetail))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.scanning).visibility)
        verify(wiFiData).wiFiDetails
    }

    @Test
    fun testScanningIsGoneWithNavigationMenuThatDoesNotHaveOptionMenu() {
        // setup
        mainActivity.currentNavigationMenu(NavigationMenu.VENDORS)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        whenever(wiFiData.connection()).thenReturn(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.scanning).visibility)
        verify(wiFiData, never()).wiFiDetails
    }

    @Test
    fun testViewCompactAddsPopup() {
        // setup
        val connection = withConnection(withWiFiAdditional())
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPACT)
        withConnectionInformation(connection)
        val view = withAccessPointDetailView(connection, ConnectionViewType.COMPACT.layout)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), connection)
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), connection)
    }

    @Test
    fun testWiFiSupportIsGoneWhenWiFiBandIsAvailable() {
        // setup
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        assertEquals(View.GONE, mainActivity.findViewById<View>(R.id.main_wifi_support).visibility)
        verify(settings).wiFiBand()
        verify(wiFiData).wiFiDetails
    }

    @Test
    fun testWiFiSupportIsVisibleWhenWiFiBandIsNotAvailable() {
        // setup
        val expectedText = mainActivity.resources.getString(WiFiBand.GHZ6.textResource)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ6)
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(false)
        whenever(settings.connectionViewType()).thenReturn(ConnectionViewType.COMPLETE)
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY))
        // execute
        fixture.update(wiFiData)
        // validate
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_support)
        assertEquals(View.VISIBLE, textView.visibility)
        assertEquals(expectedText, textView.text)
        verify(settings).wiFiBand()
        verify(wiFiManagerWrapper).is6GHzBandSupported()
        verify(wiFiData).wiFiDetails
    }

    private fun withConnection(wiFiAdditional: WiFiAdditional): WiFiDetail =
        WiFiDetail(
            WiFiIdentifier(ssid, bssid),
            String.EMPTY,
            WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55, true),
            wiFiAdditional
        )

    private fun withWiFiAdditional(): WiFiAdditional =
        WiFiAdditional(wiFiConnection = WiFiConnection(WiFiIdentifier(ssid, bssid), ipAddress, 11))

    private fun withAccessPointDetailView(connection: WiFiDetail, @LayoutRes layout: Int): View {
        val parent = mainActivity.findViewById<View>(R.id.connection).findViewById<ViewGroup>(R.id.connectionDetail)
        val view = mainActivity.layoutInflater.inflate(layout, parent, false)
        whenever(accessPointDetail.makeView(null, parent, connection, layout = layout)).thenReturn(view)
        whenever(accessPointDetail.makeView(parent.getChildAt(0), parent, connection, layout = layout)).thenReturn(view)
        return view
    }

    private fun withConnectionInformation(connection: WiFiDetail) {
        whenever(wiFiData.connection()).thenReturn(connection)
    }

    private fun verifyConnectionInformation() {
        verify(wiFiData).connection()
    }
}