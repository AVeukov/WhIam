
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.util.EMPTY

typealias SSID = String
typealias BSSID = String

data class WiFiIdentifier(val ssidRaw: SSID = String.EMPTY, val bssid: BSSID = String.EMPTY) : Comparable<WiFiIdentifier> {

    val ssid = when {
        ssidRaw.isEmpty() -> "*hidden*"
        else -> ssidRaw
    }

    val title: String
        get() = "$ssid ($bssid)"

    fun equals(other: WiFiIdentifier, ignoreCase: Boolean = false): Boolean =
        ssid.equals(other.ssidRaw, ignoreCase) && bssid.equals(other.bssid, ignoreCase)

    override fun compareTo(other: WiFiIdentifier): Int =
        compareBy<WiFiIdentifier> { it.ssidRaw }.thenBy { it.bssid }.compare(this, other)

    companion object {
        val EMPTY = WiFiIdentifier()
    }
}