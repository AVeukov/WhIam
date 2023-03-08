
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.util.EMPTY

data class WiFiDetail(
    val wiFiIdentifier: WiFiIdentifier = WiFiIdentifier.EMPTY,
    val capabilities: String = String.EMPTY,
    val wiFiSignal: WiFiSignal = WiFiSignal.EMPTY,
    val wiFiAdditional: WiFiAdditional = WiFiAdditional.EMPTY,
    val children: List<WiFiDetail> = listOf()) : Comparable<WiFiDetail> {

    constructor(wiFiDetail: WiFiDetail, wiFiAdditional: WiFiAdditional) :
            this(wiFiDetail.wiFiIdentifier, wiFiDetail.capabilities, wiFiDetail.wiFiSignal, wiFiAdditional)

    constructor(wiFiDetail: WiFiDetail, children: List<WiFiDetail>) :
            this(wiFiDetail.wiFiIdentifier, wiFiDetail.capabilities, wiFiDetail.wiFiSignal, wiFiDetail.wiFiAdditional, children)

    val security: Security
        get() = Security.findOne(capabilities)

    val securities: Set<Security>
        get() = Security.findAll(capabilities)

    val noChildren: Boolean
        get() = children.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WiFiDetail

        return wiFiIdentifier == other.wiFiIdentifier
    }

    override fun hashCode(): Int = wiFiIdentifier.hashCode()

    override fun compareTo(other: WiFiDetail): Int = wiFiIdentifier.compareTo(other.wiFiIdentifier)

    companion object {
        val EMPTY = WiFiDetail()
    }
}

