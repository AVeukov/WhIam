
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannel

@OpenClass
class ChannelRating {
    private val wiFiDetails: MutableList<WiFiDetail> = mutableListOf()

    fun count(wiFiChannel: WiFiChannel): Int = collectOverlapping(wiFiChannel).size

    fun strength(wiFiChannel: WiFiChannel): Strength =
            enumValues<Strength>()[
                    collectOverlapping(wiFiChannel)
                            .filter { !it.wiFiAdditional.wiFiConnection.connected }
                            .map { it.wiFiSignal.strength.ordinal }
                            .maxByOrNull { it } ?: Strength.ZERO.ordinal
            ]

    fun wiFiDetails(): List<WiFiDetail> = wiFiDetails

    fun wiFiDetails(wiFiDetails: List<WiFiDetail>) {
        this.wiFiDetails.clear()
        this.wiFiDetails.addAll(removeSame(wiFiDetails))
    }

    fun bestChannels(wiFiChannels: List<WiFiChannel>): List<ChannelAPCount> =
            wiFiChannels
                    .filter { bestChannel(it) }
                    .map { ChannelAPCount(it, count(it)) }
                    .sorted()

    private fun removeSame(wiFiDetails: List<WiFiDetail>): List<WiFiDetail> {
        return wiFiDetails.distinctBy { it.wiFiVirtual }.sortedWith(SortBy.STRENGTH.sort)
    }

    private fun collectOverlapping(wiFiChannel: WiFiChannel): List<WiFiDetail> =
            wiFiDetails.filter { it.wiFiSignal.inRange(wiFiChannel.frequency) }

    private fun bestChannel(it: WiFiChannel): Boolean {
        val strength: Strength = strength(it)
        return Strength.ZERO == strength || Strength.ONE == strength
    }

}