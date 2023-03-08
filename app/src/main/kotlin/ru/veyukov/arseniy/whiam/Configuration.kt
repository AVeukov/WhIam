
package ru.veyukov.arseniy.whiam

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannelPair
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannels

const val SIZE_MIN = 1024
const val SIZE_MAX = 4096

@OpenClass
class Configuration(val largeScreen: Boolean) {
    private var wiFiChannelPair = mutableMapOf<WiFiBand, WiFiChannelPair>()

    var size = SIZE_MAX

    val sizeAvailable: Boolean
        get() = size == SIZE_MAX

    fun wiFiChannelPair(countryCode: String): Unit =
        WiFiBand.values().forEach {
            this.wiFiChannelPair[it] = it.wiFiChannels.wiFiChannelPairFirst(countryCode)
        }

    fun wiFiChannelPair(wiFiBand: WiFiBand): WiFiChannelPair =
        this.wiFiChannelPair[wiFiBand]!!

    fun wiFiChannelPair(wiFiBand: WiFiBand, wiFiChannelPair: WiFiChannelPair) {
        this.wiFiChannelPair[wiFiBand] = wiFiChannelPair
    }

    init {
        WiFiBand.values().forEach {
            this.wiFiChannelPair[it] = WiFiChannels.UNKNOWN
        }

    }
}