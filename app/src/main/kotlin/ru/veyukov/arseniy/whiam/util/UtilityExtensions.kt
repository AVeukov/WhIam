package ru.veyukov.arseniy.whiam.util

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment

fun Fragment.setupActionBar(title: String, displayHome: Boolean = false, hasOptionsMenu: Boolean = false) {
    setHasOptionsMenu(hasOptionsMenu)
    (activity as? AppCompatActivity)?.invalidateOptionsMenu()
    (activity as? AppCompatActivity)?.supportActionBar?.apply {
        this.title = title
        setDisplayShowHomeEnabled(displayHome)
        setDisplayHomeAsUpEnabled(displayHome)
        this.show()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}
//fun SearchView.hideKeyboard() {
//
//    view?.let { activity?.hideKeyboard(it) }
//}
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}