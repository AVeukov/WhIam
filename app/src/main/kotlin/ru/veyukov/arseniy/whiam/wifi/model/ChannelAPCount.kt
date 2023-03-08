
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannel

data class ChannelAPCount(val wiFiChannel: WiFiChannel, val count: Int) : Comparable<ChannelAPCount> {
    override fun compareTo(other: ChannelAPCount): Int =
            compareBy<ChannelAPCount> { it.count }.thenBy { it.wiFiChannel }.compare(this, other)
}