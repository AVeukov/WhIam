
package ru.veyukov.arseniy.whiam.wifi.filter.adapter

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.settings.Settings

@OpenClass
abstract class BasicFilterAdapter<T>(open var selections: Set<T>) {
    fun selections(selections: Array<T>) {
        this.selections = selections.toSet()
    }

    abstract fun isActive(): Boolean
    abstract fun reset()
    abstract fun save(settings: Settings)
}