
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.content.DialogInterface
import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.wifi.model.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointPopup
import ru.veyukov.arseniy.whiam.wifi.model.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class AccessPointPopupTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = AccessPointPopup()

    @Test
    fun testShowOpensPopup() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        // execute
        val actual = fixture.show(view)
        // validate
        assertNotNull(actual)
        assertTrue(actual.isShowing)
    }

    @Test
    fun testPopupIsClosedOnPositiveButtonClick() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        // execute
        button.performClick()
        // validate
        RobolectricUtil.INSTANCE.clearLooper()
        assertFalse(alertDialog.isShowing)
    }

    @Test
    fun testPopupPositiveButtonIsNotVisible() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        // execute
        val actual = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        // validate
        assertEquals(View.VISIBLE, actual.visibility)
    }

    @Test
    fun testPopupNegativeButtonIsNotVisible() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        // execute
        val actual = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        // validate
        assertEquals(View.GONE, actual.visibility)
    }

    @Test
    fun testPopupNeutralButtonIsNotVisible() {
        // setup
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        val alertDialog = fixture.show(view)
        // execute
        val actual = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)
        // validate
        assertEquals(View.GONE, actual.visibility)
    }

    @Test
    fun testAttach() {
        // setup
        val wiFiDetail = withWiFiDetail()
        val view = mainActivity.layoutInflater.inflate(R.layout.access_point_view_compact, null)
        // execute
        fixture.attach(view, wiFiDetail)
        // validate
        assertTrue(view.performClick())
    }

    private fun withWiFiDetail(): WiFiDetail =
            WiFiDetail(
                    WiFiIdentifier("SSID", "BSSID"),
                    "capabilities",
                    WiFiSignal(1, 1, WiFiWidth.MHZ_40, 2, true),
                    WiFiAdditional.EMPTY)
}