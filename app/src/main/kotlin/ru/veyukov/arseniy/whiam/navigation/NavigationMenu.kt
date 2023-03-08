
package ru.veyukov.arseniy.whiam.navigation

import android.view.MenuItem
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.availability.*
import ru.veyukov.arseniy.whiam.navigation.items.*
import ru.veyukov.arseniy.whiam.navigation.availability.*
//import ru.veyukov.arseniy.whiam.navigation.availability.navigationOptionWiFiSwitchOn
import ru.veyukov.arseniy.whiam.navigation.items.*

enum class NavigationMenu(val icon: Int,
                          val title: Int,
                          val navigationItem: NavigationItem,
                          val navigationOptions: List<NavigationOption> = navigationOptionOff
) {
    SCHEME(R.drawable.ic_home, R.string.action_scheme, navigationItemScheme, navigationOptionAp),
    ACCESS_POINTS(R.drawable.ic_network_wifi, R.string.action_access_points, navigationItemAccessPoints, navigationOptionAp),
    EXPORT(R.drawable.ic_import_export, R.string.action_export, navigationItemExport),
    SETTINGS(R.drawable.ic_settings, R.string.action_settings, navigationItemSettings),
    ABOUT(R.drawable.ic_info_outline, R.string.action_about, navigationItemAbout);

    fun activateNavigationMenu(mainActivity: MainActivity, menuItem: MenuItem): Unit =
            navigationItem.activate(mainActivity, menuItem, this)

    fun activateOptions(mainActivity: MainActivity): Unit = navigationOptions.forEach { it(mainActivity) }

    //fun wiFiBandSwitchable(): Boolean = navigationOptions.contains(navigationOptionWiFiSwitchOn)

    fun registered(): Boolean = navigationItem.registered

}