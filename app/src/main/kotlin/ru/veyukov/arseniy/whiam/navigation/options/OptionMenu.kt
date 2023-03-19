
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
        activity.menuInflater.inflate(R.menu.optionmenu, menu)
        createSearchView(activity, menu)
        this.menu = menu
        iconsVisible(menu)
    }
    fun createSearchView(activity: Activity, menu: Menu){
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        if (searchItem != null) {
            searchView = searchItem?.actionView as SearchView
            searchView?.queryHint = activity.resources.getString(R.string.search_hint)
            searchView?.findViewById<AutoCompleteTextView>(R.id.search_src_text)?.threshold = 0

            val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
            val to = intArrayOf(R.id.search_item_label)
            val cursorAdapter = SimpleCursorAdapter(activity.applicationContext, R.layout.search_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
            val suggestions = MainContext.INSTANCE.scheme.getSearchList()
            searchView?.suggestionsAdapter = cursorAdapter
            searchView?.setIconified(false);
            searchView?.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    return true
                }
            })
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    activity.hideKeyboard()
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                    var idx = -1
                    query?.let {
                        suggestions?.forEachIndexed { index, suggestion ->
                            if (suggestion.contains(query, true)) {
                                cursor.addRow(arrayOf(index, suggestion))
                                idx = index
                            }
                        }
                    }
                    if (cursor.count == 1) MainContext.INSTANCE.scheme.processSearchResult(idx)
                    cursorAdapter.changeCursor(cursor)
                    return true
                }
            })

            searchView?.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return false
                }

                @SuppressLint("Range")
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