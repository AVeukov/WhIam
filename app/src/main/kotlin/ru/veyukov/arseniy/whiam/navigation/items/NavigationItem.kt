
package ru.veyukov.arseniy.whiam.navigation.items

import android.view.MenuItem
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu

interface NavigationItem {
    fun activate(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu)
    val registered: Boolean
        get() = false
    val visibility: Int
        get() = android.view.View.GONE
}