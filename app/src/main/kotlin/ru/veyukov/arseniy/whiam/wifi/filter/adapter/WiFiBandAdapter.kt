
package ru.veyukov.arseniy.whiam.wifi.filter.adapter

import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand

class WiFiBandAdapter(values: Set<WiFiBand>) : EnumFilterAdapter<WiFiBand>(values, WiFiBand.values()) {
    override fun save(settings: Settings): Unit =
            settings.saveWiFiBands(selections)
}