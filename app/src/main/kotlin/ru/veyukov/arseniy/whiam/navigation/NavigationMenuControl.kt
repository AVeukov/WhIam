
package ru.veyukov.arseniy.whiam.navigation

import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

interface NavigationMenuControl : NavigationView.OnNavigationItemSelectedListener,
    NavigationBarView.OnItemSelectedListener {
    fun currentMenuItem(): MenuItem
    fun currentNavigationMenu(): NavigationMenu
    fun currentNavigationMenu(navigationMenu: NavigationMenu)
    fun navigationView(): NavigationView
    fun <T : View?> findViewById(@IdRes id: Int): T
}