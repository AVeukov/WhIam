
package ru.veyukov.arseniy.whiam.scheme

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointsAdapterData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.scanner.UpdateNotifier

@OpenClass
class SchemeAdapter( //конструктор класса
    private val view: SchemeView, // view
    private val accessPointsAdapterData: AccessPointsAdapterData = AccessPointsAdapterData() // данные адаптера WiFi, создаются тут
) :UpdateNotifier {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun update(wiFiData: WiFiData) {
        // обновление данных Wi-Fi
        accessPointsAdapterData.update(wiFiData, null)
        // перерисовка SchemeView
        view.redraw()
    }


}