
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.net.wifi.WifiInfo
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.veyukov.arseniy.whiam.util.buildMinVersionP
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.model.WiFiConnection
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.scanner.UpdateNotifier

class ConnectionView(
    private val mainActivity: MainActivity,
    private val accessPointDetail: AccessPointDetail = AccessPointDetail(),
    private val accessPointPopup: AccessPointPopup = AccessPointPopup()
) : UpdateNotifier {

    override fun update(wiFiData: WiFiData) {
        val settings = MainContext.INSTANCE.settings
        displayConnection(wiFiData, settings)
        displayWiFiSupport(settings)
        displayNoData(wiFiData)
    }

    private fun displayWiFiSupport(settings: Settings) {
        val wiFiBand = settings.wiFiBand()
        val visibility = if (wiFiBand.available()) View.GONE else View.VISIBLE
        val textView = mainActivity.findViewById<TextView>(R.id.main_wifi_support)
        textView.visibility = visibility
        textView.text = mainActivity.resources.getString(wiFiBand.textResource)
    }

    private fun displayNoData(wiFiData: WiFiData) {
        val visibility = if (noData(wiFiData)) View.VISIBLE else View.GONE
        mainActivity.findViewById<View>(R.id.scanning).visibility = visibility
        mainActivity.findViewById<View>(R.id.no_data).visibility = visibility
        mainActivity.findViewById<View>(R.id.no_location).visibility = getNoLocationVisibility(visibility)
        if (buildMinVersionP()) {
            mainActivity.findViewById<View>(R.id.throttling).visibility = visibility
        }
    }

    private fun getNoLocationVisibility(visibility: Int): Int =
        if (mainActivity.permissionService.enabled()) View.GONE else visibility

    private fun noData(wiFiData: WiFiData): Boolean =
        mainActivity.currentNavigationMenu().registered() && wiFiData.wiFiDetails.isEmpty()

    private fun displayConnection(wiFiData: WiFiData, settings: Settings) {
        val connectionViewType = settings.connectionViewType()
        val connection = wiFiData.connection()
        val connectionView = mainActivity.findViewById<View>(R.id.connection)
        val wiFiConnection = connection.wiFiAdditional.wiFiConnection
        if (connectionViewType.hide || !wiFiConnection.connected) {
            connectionView.visibility = View.GONE
        } else {
            connectionView.visibility = View.VISIBLE
            val parent = connectionView.findViewById<ViewGroup>(R.id.connectionDetail)
            val view =
                accessPointDetail.makeView(parent.getChildAt(0), parent, connection, layout = connectionViewType.layout)
            if (parent.childCount == 0) {
                parent.addView(view)
            }
            setViewConnection(connectionView, wiFiConnection)
            attachPopup(view, connection)
        }
    }

    private fun setViewConnection(connectionView: View, wiFiConnection: WiFiConnection) {
        val ipAddress = wiFiConnection.ipAddress
        connectionView.findViewById<TextView>(R.id.ipAddress).text = ipAddress
        val textLinkSpeed = connectionView.findViewById<TextView>(R.id.linkSpeed)
        val linkSpeed = wiFiConnection.linkSpeed
        if (linkSpeed == WiFiConnection.LINK_SPEED_INVALID) {
            textLinkSpeed.visibility = View.GONE
        } else {
            textLinkSpeed.visibility = View.VISIBLE
            textLinkSpeed.text = "$linkSpeed${WifiInfo.LINK_SPEED_UNITS}"
        }
    }

    private fun attachPopup(view: View, wiFiDetail: WiFiDetail) {
        view.findViewById<View>(R.id.attachPopup)?.let {
            accessPointPopup.attach(it, wiFiDetail)
            accessPointPopup.attach(view.findViewById(R.id.ssid), wiFiDetail)
        }
    }

}