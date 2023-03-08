
package ru.veyukov.arseniy.whiam.navigation.items

import android.view.View
import ru.veyukov.arseniy.whiam.about.AboutFragment
import ru.veyukov.arseniy.whiam.export.Export
import ru.veyukov.arseniy.whiam.scheme.SchemeFragment
import ru.veyukov.arseniy.whiam.settings.SettingsFragment
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointsFragment

val navigationItemAccessPoints: NavigationItem = FragmentItem(AccessPointsFragment())
val navigationItemExport: NavigationItem = ExportItem(Export())
val navigationItemSettings: NavigationItem = FragmentItem(SettingsFragment(), false, View.GONE)
val navigationItemAbout: NavigationItem = FragmentItem(AboutFragment(), false, View.GONE)
val navigationItemScheme: NavigationItem = FragmentItem(SchemeFragment())