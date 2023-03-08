
package ru.veyukov.arseniy.whiam.wifi.filter.adapter

import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.model.Strength

class StrengthAdapter(selections: Set<Strength>) : EnumFilterAdapter<Strength>(selections, Strength.values()) {
//    override fun color(selection: Strength): Int =
//            if (selections.contains(selection)) selection.colorResource else Strength.colorResourceDefault

    override fun save(settings: Settings): Unit =
            settings.saveStrengths(selections)
}