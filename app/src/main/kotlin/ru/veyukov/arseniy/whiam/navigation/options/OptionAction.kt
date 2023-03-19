
package ru.veyukov.arseniy.whiam.navigation.options

import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand

typealias Action = () -> Unit

internal val noAction: Action = { }

internal val scannerAction: Action = { MainContext.INSTANCE.scannerService.toggle() }
internal val pathAction: Action = { MainContext.INSTANCE.scheme.pathAction() }

internal enum class OptionAction(val key: Int, val action: Action) {
    NO_ACTION(-1, noAction),
    SCANNER(R.id.action_scanner, scannerAction),
    PATH(R.id.action_path, pathAction);

    companion object {
        fun findOptionAction(key: Int): OptionAction {
            for (value in values()) {
                if (value.key == key) return value
            }
            return NO_ACTION
        }
    }

}