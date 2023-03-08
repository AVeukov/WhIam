
package ru.veyukov.arseniy.whiam.navigation.items

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu

internal class FragmentItem(val fragment: Fragment, override val registered: Boolean = true, override val visibility: Int = View.VISIBLE) :
    NavigationItem {

    override fun activate(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu) {
        val fragmentManager: FragmentManager = mainActivity.supportFragmentManager
        if (fragmentManager.isStateSaved) return
        updateMainActivity(mainActivity, menuItem, navigationMenu)
        startFragment(fragmentManager)
    }

    private fun startFragment(fragmentManager: FragmentManager) {
        fragmentManager.commit {
            replace(R.id.main_fragment, fragment)
        }
    }

    private fun updateMainActivity(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu) {
        mainActivity.currentNavigationMenu(navigationMenu)
        if (navigationMenu == NavigationMenu.SCHEME)
            mainActivity.title = MainContext.INSTANCE.scheme.getAllFloors(MainContext.INSTANCE.scheme.currentBuilding)[MainContext.INSTANCE.scheme.currentFloor]
        else mainActivity.title = menuItem.title
        mainActivity.updateActionBar()
        mainActivity.mainConnectionVisibility(visibility)
    }

}