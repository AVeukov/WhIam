

package ru.veyukov.arseniy.whiam

import androidx.activity.OnBackPressedCallback

class MainActivityBackPressed(val mainActivity: MainActivity) : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        if (!mainActivity.closeDrawer()) {
            val selectedMenu = MainContext.INSTANCE.settings.selectedMenu()
            if (selectedMenu == mainActivity.currentNavigationMenu()) {
                mainActivity.finish()
            } else {
                mainActivity.currentNavigationMenu(selectedMenu)
                mainActivity.onNavigationItemSelected(mainActivity.currentMenuItem())
            }
        }
    }
}
