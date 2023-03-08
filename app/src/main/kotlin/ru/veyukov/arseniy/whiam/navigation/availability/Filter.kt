
package ru.veyukov.arseniy.whiam.navigation.availability

import androidx.core.content.ContextCompat
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R

internal val navigationOptionFilterOff: NavigationOption = {
    it.optionMenu.menu?.let { menu -> menu.findItem(R.id.action_filter).isVisible = false }
}

internal val navigationOptionFilterOn: NavigationOption = {
    it.optionMenu.menu?.let { menu ->
        val color = if (MainContext.INSTANCE.filtersAdapter.isActive()) R.color.selected else R.color.regular
        val menuItem = menu.findItem(R.id.action_filter)
        menuItem.isVisible = true
        menuItem.icon?.setTint(ContextCompat.getColor(it, color))
    }
}
