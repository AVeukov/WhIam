
package ru.veyukov.arseniy.whiam.navigation.options


import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.view.Menu
import android.view.MenuItem
import android.widget.AutoCompleteTextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.hideKeyboard


@OpenClass
class OptionMenu {
    var menu: Menu? = null
    var searchView: SearchView? = null

    fun create(activity: Activity, menu: Menu) {
        // построение меню по файлу optionmenu.xml из ресурсов
        activity.menuInflater.inflate(R.menu.optionmenu, menu)
        // создание виджета поиска androidx.appcompat.widget.SearchView
        createSearchView(activity, menu)
        this.menu = menu
        iconsVisible(menu)
    }
    fun createSearchView(activity: Activity, menu: Menu){
        // поиск виджета поиска androidx.appcompat.widget.SearchView
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        if (searchItem != null) { // создание SearchView
            searchView = searchItem?.actionView as SearchView
            searchView?.queryHint = activity.resources.getString(R.string.search_hint) // подсказка
            // поиск стандартного поля поиска
            searchView?.findViewById<AutoCompleteTextView>(R.id.search_src_text)?.threshold = 0 // минимум символов в строке для отображения предложений

            val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1) //стандарт
            val to = intArrayOf(R.id.search_item_label) // стандарт
            // адаптер курсора поиска
            val cursorAdapter = SimpleCursorAdapter(activity.applicationContext, R.layout.search_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
            // предлагаемые значения
            val suggestions = MainContext.INSTANCE.scheme.getSearchList() // список этажей и комнат со схемы JSON
            // параметры SearchView
            searchView?.suggestionsAdapter = cursorAdapter
            searchView?.setIconified(false);
            // обработчик закрытия
            searchView?.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    return true
                }
            })
            // обработчик запросов поиска
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                // передача текста поиска
                override fun onQueryTextSubmit(query: String?): Boolean {
                    activity.hideKeyboard()
                    return false
                }
                // текст поиска изменен
                override fun onQueryTextChange(query: String?): Boolean {
                    val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                    var idx = -1
                    query?.let {// поиск текста без учета регистра букв
                        suggestions?.forEachIndexed { index, suggestion ->
                            if (suggestion.contains(query, true)) {
                                cursor.addRow(arrayOf(index, suggestion))
                                idx = index
                            }
                        }
                    }
                    // если выбрана одна строка, то выполняется обработка в Scheme с последующей отрисовкой комнаты на экране
                    if (cursor.count == 1) MainContext.INSTANCE.scheme.processSearchResult(idx)
                    cursorAdapter.changeCursor(cursor)
                    return true
                }
            })

            searchView?.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
                // обработчик выбора позиции
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                @SuppressLint("Range")
                // Обработчик Клика по выбранной строке
                override fun onSuggestionClick(position: Int): Boolean {
                    activity.hideKeyboard()
                    val cursor = searchView?.suggestionsAdapter?.getItem(position) as Cursor
                    val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                    searchView?.setQuery(selection, false)
                    return true
                }
            })
        }
    }
    // действия меню
    fun select(item: MenuItem): Unit = OptionAction.findOptionAction(item.itemId).action()


    @SuppressLint("RestrictedApi")
    private fun iconsVisible(menu: Menu) {
        try {
            (menu as MenuBuilder).setOptionalIconsVisible(true)
        } catch (e: Exception) {
            // do nothing
        }
    }
}