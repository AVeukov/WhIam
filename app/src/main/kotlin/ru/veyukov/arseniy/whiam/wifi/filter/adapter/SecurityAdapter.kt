
package ru.veyukov.arseniy.whiam.wifi.filter.adapter

import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.model.Security

class SecurityAdapter(selections: Set<Security>) : EnumFilterAdapter<Security>(selections, Security.values()) {
    override fun save(settings: Settings) {
        settings.saveSecurities(selections)
    }
}