
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
            MainContext.INSTANCE.scheme.recalcCurrentPosition()
        } else {
            menuItem.setTitle(R.string.scanner_play)
            menuItem.setIcon(R.drawable.ic_play_arrow)
            MainContext.INSTANCE.scheme.currentPosition = -1
        }
    }
}
internal val navigationOptionPathOff: NavigationOption = {
    it.optionMenu.menu?.let { menu ->
        menu.findItem(R.id.action_path).isVisible = false
    }
    it.invalidateOptionsMenu()
}
internal val navigationOptionPathOn: NavigationOption = {
    it.optionMenu.menu?.let { menu ->
        val menuItem = menu.findItem(R.id.action_path)
        menuItem.isVisible = true
        if ( MainContext.INSTANCE.scheme.pathMode == 1 ) {
            menuItem.setIcon(R.drawable.ic_path_on)
        } else {
            menuItem.setIcon(R.drawable.ic_path)
        }
    }
    it.invalidateOptionsMenu()
}
