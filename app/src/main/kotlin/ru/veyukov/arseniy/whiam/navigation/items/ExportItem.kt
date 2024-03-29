
package ru.veyukov.arseniy.whiam.navigation.items

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.export.Export
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.scheme.data.NodeData

internal class ExportItem(private val export: Export) : NavigationItem {
    // активация меню (выбор)
    override fun activate(mainActivity: MainActivity, menuItem: MenuItem, navigationMenu: NavigationMenu) {
        // список контрольных точек
        val nodes: List<NodeData> = MainContext.INSTANCE.scheme.nodeList
        if (nodes.isEmpty()) { // если пусто - сообщение
            Toast.makeText(mainActivity, R.string.no_data, Toast.LENGTH_LONG).show()
            return
        }
        // создаем Intent
        val intent: Intent = export.export(mainActivity, nodes)
        if (!exportAvailable(mainActivity, intent)) { // проверка на доступность
            Toast.makeText(mainActivity, R.string.export_not_available, Toast.LENGTH_LONG).show()
            return
        }
        try {
            // запуск экспорта
            mainActivity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(mainActivity, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
    // Проверка на доступность
    private fun exportAvailable(mainActivity: MainActivity, chooser: Intent): Boolean =
        chooser.resolveActivity(mainActivity.packageManager) != null

}