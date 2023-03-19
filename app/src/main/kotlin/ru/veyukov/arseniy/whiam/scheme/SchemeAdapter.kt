
package ru.veyukov.arseniy.whiam.scheme

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointsAdapterData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.scanner.UpdateNotifier

@OpenClass
class SchemeAdapter(
    private val view: SchemeView,
    private val accessPointsAdapterData: AccessPointsAdapterData = AccessPointsAdapterData()
) :UpdateNotifier {

    override fun update(wiFiData: WiFiData) {
        accessPointsAdapterData.update(wiFiData, null)
        view.redraw()
    }


}