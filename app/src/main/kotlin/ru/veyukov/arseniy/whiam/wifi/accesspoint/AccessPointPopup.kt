
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.app.AlertDialog
import android.view.View
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail

@OpenClass
class AccessPointPopup {
    fun show(view: View): AlertDialog {
        val alertDialog: AlertDialog = AlertDialog.Builder(view.context)
                .setView(view)
                .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.cancel(); }
                .create()
        alertDialog.show()
        return alertDialog
    }

    fun attach(view: View, wiFiDetail: WiFiDetail) {
        view.setOnClickListener {
            try {
                show(AccessPointDetail().makeViewDetailed(wiFiDetail))
            } catch (e: Exception) {
                // do nothing
            }
        }
    }

}