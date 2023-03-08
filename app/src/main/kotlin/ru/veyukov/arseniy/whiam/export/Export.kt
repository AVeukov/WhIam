

package ru.veyukov.arseniy.whiam.export

import android.content.Intent
import android.content.res.Resources
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.model.WiFiSignal.Companion.FREQUENCY_UNITS
import java.text.SimpleDateFormat
import java.util.*

class Export(private val exportIntent: ExportIntent = ExportIntent()) {

    private val header = "Time Stamp|" +
            "SSID|" +
            "BSSID|" +
            "Strength|" +
            "Primary Channel|" +
            "Primary Frequency|" +
            "Center Channel|" +
            "Center Frequency|" +
            "Width (Range)|" +
            "Distance|" +
            "Timestamp|" +
            "802.11mc|" +
            "Security" +
            "\n"

    fun export(mainActivity: MainActivity, wiFiDetails: List<WiFiDetail>): Intent =
            export(mainActivity, wiFiDetails, Date())

    fun export(mainActivity: MainActivity, wiFiDetails: List<WiFiDetail>, date: Date): Intent {
        val timestamp: String = timestamp(date)
        val title: String = title(mainActivity, timestamp)
        val data: String = data(wiFiDetails, timestamp)
        return exportIntent.intent(title, data)
    }

    internal fun data(wiFiDetails: List<WiFiDetail>, timestamp: String): String =
            header + wiFiDetails.joinToString(separator = String.EMPTY, transform = toExportString(timestamp))

    internal fun title(mainActivity: MainActivity, timestamp: String): String {
        val resources: Resources = mainActivity.resources
        val title: String = resources.getString(R.string.action_access_points)
        return "$title-$timestamp"
    }

    internal fun timestamp(date: Date): String = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(date)

    private fun toExportString(timestamp: String): (WiFiDetail) -> String = {
        with(it) {
            "$timestamp|" +
                    "${wiFiIdentifier.ssid}|" +
                    "${wiFiIdentifier.bssid}|" +
                    "${wiFiSignal.level}dBm|" +
                    "${wiFiSignal.primaryWiFiChannel.channel}|" +
                    "${wiFiSignal.primaryFrequency}$FREQUENCY_UNITS|" +
                    "${wiFiSignal.centerWiFiChannel.channel}|" +
                    "${wiFiSignal.centerFrequency}$FREQUENCY_UNITS|" +
                    "${wiFiSignal.wiFiWidth.frequencyWidth}$FREQUENCY_UNITS (${wiFiSignal.frequencyStart} - ${wiFiSignal.frequencyEnd})|" +
                    "${wiFiSignal.distance}|" +
                    "${wiFiSignal.timestamp}|" +
                    "${wiFiSignal.is80211mc}|" +
                    capabilities +
                    "\n"
        }
    }

    companion object {
        private const val TIME_STAMP_FORMAT = "yyyy/MM/dd-HH:mm:ss"
    }

}