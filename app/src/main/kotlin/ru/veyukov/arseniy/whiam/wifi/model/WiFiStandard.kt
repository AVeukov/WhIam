
package ru.veyukov.arseniy.whiam.wifi.model

import android.net.wifi.ScanResult
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.veyukov.arseniy.whiam.util.buildMinVersionR
import ru.veyukov.arseniy.whiam.util.buildMinVersionS
import ru.veyukov.arseniy.whiam.util.buildMinVersionT
import ru.veyukov.arseniy.whiam.R

typealias WiFiStandardId = Int

private val unknown: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_UNKNOWN else 0
private val legacy: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_LEGACY else 1
private val n: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_11N else 4
private val ac: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_11AC else 5
private val ax: WiFiStandardId = if (buildMinVersionR()) ScanResult.WIFI_STANDARD_11AX else 6
private val ad: WiFiStandardId = if (buildMinVersionS()) ScanResult.WIFI_STANDARD_11AD else 7
private val be: WiFiStandardId = if (buildMinVersionT()) ScanResult.WIFI_STANDARD_11BE else 8


enum class WiFiStandard(
    val wiFiStandardId: WiFiStandardId,
    @StringRes val textResource: Int,
    @DrawableRes val imageResource: Int
) {
    UNKNOWN(unknown, R.string.wifi_standard_unknown, R.drawable.ic_wifi_unknown),
    LEGACY(legacy, R.string.wifi_standard_legacy, R.drawable.ic_wifi_legacy),
    N(n, R.string.wifi_standard_n, R.drawable.ic_wifi_4),
    AC(ac, R.string.wifi_standard_ac, R.drawable.ic_wifi_5),
    AX(ax, R.string.wifi_standard_ax, R.drawable.ic_wifi_6),
    AD(ad, R.string.wifi_standard_ad, R.drawable.ic_wifi_unknown),
    BE(be, R.string.wifi_standard_be, R.drawable.ic_wifi_unknown);

    companion object {
        fun findOne(wiFiStandardId: WiFiStandardId): WiFiStandard =
            values().firstOrNull { it.wiFiStandardId == wiFiStandardId } ?: UNKNOWN
    }
}