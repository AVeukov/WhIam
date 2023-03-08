
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.model.WiFiAdditional
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.model.WiFiSignal
import java.text.SimpleDateFormat
import java.util.*

@OpenClass
class AccessPointDetail {
    private val vendorShortMax = 12
    private val vendorLongMax = 30

    fun makeView(convertView: View?,
                 parent: ViewGroup?,
                 wiFiDetail: WiFiDetail,
                 child: Boolean = false,
                 @LayoutRes layout: Int = MainContext.INSTANCE.settings.accessPointView().layout)
            : View {
        val view = convertView ?: MainContext.INSTANCE.layoutInflater.inflate(layout, parent, false)
        setViewCompact(view, wiFiDetail, child)
        setViewExtra(view, wiFiDetail)
        setViewVendorShort(view, wiFiDetail.wiFiAdditional)
        return view
    }

    fun makeViewDetailed(wiFiDetail: WiFiDetail): View {
        val view = MainContext.INSTANCE.layoutInflater.inflate(R.layout.access_point_view_popup, null)
        setViewCompact(view, wiFiDetail, false)
        setViewExtra(view, wiFiDetail)
        setViewCapabilitiesLong(view, wiFiDetail)
        setViewVendorLong(view, wiFiDetail.wiFiAdditional)
        setViewWiFiBand(view, wiFiDetail.wiFiSignal)
        setView80211mc(view, wiFiDetail.wiFiSignal)
        setViewWiFiStandard(view, wiFiDetail.wiFiSignal)
        setTimestamp(view, wiFiDetail.wiFiSignal)
        enableTextSelection(view)
        return view
    }

    private fun enableTextSelection(view: View) =
        view.findViewById<TextView>(R.id.ssid)?.let {
            it.setTextIsSelectable(true)
            view.findViewById<TextView>(R.id.vendorLong).setTextIsSelectable(true)
        }

    private fun setViewCompact(view: View, wiFiDetail: WiFiDetail, child: Boolean) =
        view.findViewById<TextView>(R.id.ssid)?.let {
            it.text = wiFiDetail.wiFiIdentifier.title
            val wiFiSignal = wiFiDetail.wiFiSignal
            view.findViewById<TextView>(R.id.channel).text = wiFiSignal.channelDisplay()
            view.findViewById<TextView>(R.id.primaryFrequency).text =
                "${wiFiSignal.primaryFrequency}${WiFiSignal.FREQUENCY_UNITS}"
            view.findViewById<TextView>(R.id.distance).text = wiFiSignal.distance
            view.findViewById<View>(R.id.tab).visibility = if (child) View.VISIBLE else View.GONE
            setSecurityImage(view, wiFiDetail)
            setLevelText(view, wiFiSignal)
        }

    private fun setSecurityImage(view: View, wiFiDetail: WiFiDetail) =
        view.findViewById<ImageView>(R.id.securityImage)?.let {
            val security = wiFiDetail.security
            it.tag = security.imageResource
            it.setImageResource(security.imageResource)
        }


    private fun setViewExtra(view: View, wiFiDetail: WiFiDetail) =
        view.findViewById<TextView>(R.id.channel_frequency_range)?.let {
            val wiFiSignal = wiFiDetail.wiFiSignal
            setLevelImage(view, wiFiSignal)
            setWiFiStandardImage(view, wiFiSignal)
            it.text = "${wiFiSignal.frequencyStart} - ${wiFiSignal.frequencyEnd}"
            view.findViewById<TextView>(R.id.width).text =
                "(${wiFiSignal.wiFiWidth.frequencyWidth}${WiFiSignal.FREQUENCY_UNITS})"
            view.findViewById<TextView>(R.id.capabilities).text = wiFiDetail.securities
                .toList()
                .joinToString(" ", "[", "]")
        }

    private fun setWiFiStandardImage(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<ImageView>(R.id.wiFiStandardImage)?.let {
            it.tag = wiFiSignal.wiFiStandard.imageResource
            it.setImageResource(wiFiSignal.wiFiStandard.imageResource)
        }

    private fun setLevelText(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.level)?.let {
            it.text = "${wiFiSignal.level}dBm"
            it.setTextColor(ContextCompat.getColor(view.context, wiFiSignal.strength.colorResource))
        }

    private fun setLevelImage(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<ImageView>(R.id.levelImage)?.let {
            val strength = wiFiSignal.strength
            it.tag = strength.imageResource
            it.setImageResource(strength.imageResource)
            it.setColorFilter(ContextCompat.getColor(view.context, strength.colorResource))
        }

    private fun setViewVendorShort(view: View, wiFiAdditional: WiFiAdditional) =
        view.findViewById<TextView>(R.id.vendorShort)?.let {
            if (wiFiAdditional.vendorName.isBlank()) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = wiFiAdditional.vendorName.take(vendorShortMax)
            }
        }

    private fun setViewCapabilitiesLong(view: View, wiFiDetail: WiFiDetail) =
        view.findViewById<TextView>(R.id.capabilitiesLong)?.let {
            it.text = wiFiDetail.capabilities
        }

    private fun setViewVendorLong(view: View, wiFiAdditional: WiFiAdditional) =
        view.findViewById<TextView>(R.id.vendorLong)?.let {
            if (wiFiAdditional.vendorName.isBlank()) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = wiFiAdditional.vendorName.take(vendorLongMax)
            }
        }

    private fun setViewWiFiBand(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.wiFiBand)?.setText(wiFiSignal.wiFiBand.textResource)

    private fun setViewWiFiStandard(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.wiFiStandard)
            ?.setText(wiFiSignal.wiFiStandard.textResource)

    private fun setView80211mc(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.flag80211mc)?.let {
            it.visibility = if (wiFiSignal.is80211mc) View.VISIBLE else View.GONE
        }

    private fun setTimestamp(view: View, wiFiSignal: WiFiSignal) =
        view.findViewById<TextView>(R.id.timestamp)?.let {
            val milliseconds: Long = wiFiSignal.timestamp / 1000
            if (0L == milliseconds) {
                it.text = String.EMPTY
                it.visibility = View.GONE
            } else {
                val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US)
                simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
                it.text = simpleDateFormat.format(Date(milliseconds))
                it.visibility = View.VISIBLE
            }
        }

    companion object {
        private const val TIME_STAMP_FORMAT = "H:mm:ss.SSS"
    }

}