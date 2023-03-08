
package ru.veyukov.arseniy.whiam.wifi.filter

import android.app.AlertDialog
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.filter.adapter.WiFiBandAdapter

internal class WiFiBandFilter(wiFiBandAdapter: WiFiBandAdapter, alertDialog: AlertDialog) :
        EnumFilter<WiFiBand, WiFiBandAdapter>(
                mapOf(
                    WiFiBand.GHZ2 to R.id.filterWifiBand2,
                    WiFiBand.GHZ5 to R.id.filterWifiBand5,
                    WiFiBand.GHZ6 to R.id.filterWifiBand6
                ),
                wiFiBandAdapter,
                alertDialog,
                R.id.filterWiFiBand
        )