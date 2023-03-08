
package ru.veyukov.arseniy.whiam.wifi.model

import android.net.wifi.ScanResult
import kotlin.math.abs

typealias ChannelWidth = Int

typealias CalculateCenter = (primary: Int, center: Int) -> Int

internal val calculateCenter20: CalculateCenter = { primary, _ -> primary }

internal val calculateCenter40: CalculateCenter = { primary, center ->
    if (abs(primary - center) >= WiFiWidth.MHZ_40.frequencyWidthHalf) {
        (primary + center) / 2
    } else {
        center
    }
}

internal val calculateCenter80: CalculateCenter = { _, center -> center }

internal val calculateCenter160: CalculateCenter = { primary, center ->
    when (primary) {
        // 5GHz
        in 5170..5330 -> 5250
        in 5490..5730 -> 5570
        in 5735..5895 -> 5815
        // 6GHz
        in 5950..6100 -> 6025
        in 6110..6260 -> 6185
        in 6270..6420 -> 6345
        in 6430..6580 -> 6505
        in 6590..6740 -> 6665
        in 6750..6900 -> 6825
        in 6910..7120 -> 6985
        else -> center
    }
}

enum class WiFiWidth(val channelWidth: ChannelWidth, val frequencyWidth: Int, val guardBand: Int, val calculateCenter: CalculateCenter) {
    MHZ_20(ScanResult.CHANNEL_WIDTH_20MHZ, 20, 2, calculateCenter20),
    MHZ_40(ScanResult.CHANNEL_WIDTH_40MHZ, 40, 3, calculateCenter40),
    MHZ_80(ScanResult.CHANNEL_WIDTH_80MHZ, 80, 3, calculateCenter80),
    MHZ_160(ScanResult.CHANNEL_WIDTH_160MHZ, 160, 3, calculateCenter160),
    MHZ_80_PLUS(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ, 80, 3, calculateCenter80);

    val frequencyWidthHalf: Int = frequencyWidth / 2

    companion object {
        fun findOne(channelWidth: ChannelWidth): WiFiWidth =
            values().firstOrNull { it.channelWidth == channelWidth } ?: MHZ_20
    }
}
