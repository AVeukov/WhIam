
package ru.veyukov.arseniy.whiam.wifi.filter

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.veyukov.arseniy.whiam.wifi.filter.adapter.EnumFilterAdapter

internal abstract class EnumFilter<T : Enum<*>, U : EnumFilterAdapter<T>>(internal val ids: Map<T, Int>, private val filter: U, alertDialog: AlertDialog, id: Int) {

    private fun setColor(view: View, value: T) {
        this.filter.color(value).let {
            val color = ContextCompat.getColor(view.context, it)
            when (view) {
                is TextView -> view.setTextColor(color)
                is ImageView -> view.setColorFilter(color)
            }
        }
    }

    init {
        ids.keys.forEach { value -> ids[value]?.let { process(alertDialog, it, value) } }
        alertDialog.findViewById<View>(id).visibility = View.VISIBLE
    }

    private fun process(alertDialog: AlertDialog, id: Int, value: T) {
        val view = alertDialog.findViewById<View>(id)
        view.setOnClickListener { onClickListener(value, it) }
        setColor(view, value)
    }

    private fun onClickListener(value: T, view: View) {
        filter.toggle(value)
        setColor(view, value)
    }
}