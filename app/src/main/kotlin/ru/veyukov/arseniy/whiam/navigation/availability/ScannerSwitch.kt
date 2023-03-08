
package ru.veyukov.arseniy.whiam.navigation.availability

import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R

internal val navigationOptionScannerSwitchOff: NavigationOption = {
    it.optionMenu.menu?.let { menu ->
        menu.findItem(R.id.action_scanner).isVisible = false
    }
}

internal val navigationOptionScannerSwitchOn: NavigationOption = {
    it.optionMenu.menu?.let { menu ->
        val menuItem = menu.findItem(R.id.action_scanner)
        menuItem.isVisible = true
        if (MainContext.INSTANCE.scannerService.running()) {
            menuItem.setTitle(R.string.scanner_pause)
            menuItem.setIcon(R.drawable.ic_pause)
        } else {
            menuItem.setTitle(R.string.scanner_play)
            menuItem.setIcon(R.drawable.ic_play_arrow)
        }
    }
}
