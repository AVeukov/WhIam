
package ru.veyukov.arseniy.whiam.navigation

import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.R

@OpenClass
class NavigationMenuController(
    navigationMenuControl: NavigationMenuControl,
    val navigationView: NavigationView = navigationMenuControl.findViewById(R.id.nav_drawer),
    //val bottomNavigationView: BottomNavigationView = navigationMenuControl.findViewById(R.id.nav_bottom)
) {

    private lateinit var currentNavigationMenu: NavigationMenu

    fun currentMenuItem(): MenuItem = navigationView.menu.getItem(currentNavigationMenu.ordinal)

    fun currentNavigationMenu(): NavigationMenu = currentNavigationMenu

    fun currentNavigationMenu(navigationMenu: NavigationMenu) {
        currentNavigationMenu = navigationMenu
        selectCurrentMenuItem(navigationMenu, navigationView.menu)
        //selectCurrentMenuItem(navigationMenu, bottomNavigationView.menu)
    }

    private fun selectCurrentMenuItem(navigationMenu: NavigationMenu, menu: Menu) {
        for (i in 0 until menu.size()) {
            val menuItem: MenuItem = menu.getItem(i)
            menuItem.isCheckable = false
            menuItem.isChecked = false
        }
        menu.findItem(navigationMenu.ordinal)?.let {
            it.isCheckable = true
            it.isChecked = true
        }
    }

    init {
        NavigationGroup.values().forEach { it.populateMenuItems(navigationView.menu) }
        navigationView.setNavigationItemSelectedListener(navigationMenuControl)
        //NavigationGroup.GROUP_FEATURE.populateMenuItems(bottomNavigationView.menu)
        //bottomNavigationView.setOnItemSelectedListener(navigationMenuControl)
    }
}