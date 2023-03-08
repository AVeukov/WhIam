
package ru.veyukov.arseniy.whiam.wifi.band

import androidx.annotation.StringRes
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R

typealias Available = () -> Boolean

internal val availableGHZ2: Available = { true }
internal val availableGHZ5: Available = { MainContext.INSTANCE.wiFiManagerWrapper.is5GHzBandSupported() }
internal val availableGHZ6: Available = { MainContext.INSTANCE.wiFiManagerWrapper.is6GHzBandSupported() }

enum class WiFiBand(@StringRes val textResource: Int, val wiFiChannels: WiFiChannels, val available: Available) {
    GHZ2(R.string.wifi_band_2ghz, WiFiChannelsGHZ2(), availableGHZ2),
    GHZ5(R.string.wifi_band_5ghz, WiFiChannelsGHZ5(), availableGHZ5),
    GHZ6(R.string.wifi_band_6ghz, WiFiChannelsGHZ6(), availableGHZ6);

    val ghz2: Boolean get() = GHZ2 == this
    val ghz5: Boolean get() = GHZ5 == this
    val ghz6: Boolean get() = GHZ6 == this

    companion object {
        fun find(frequency: Int): WiFiBand = values().find { it.wiFiChannels.inRange(frequency) }
            ?: GHZ2
    }

}