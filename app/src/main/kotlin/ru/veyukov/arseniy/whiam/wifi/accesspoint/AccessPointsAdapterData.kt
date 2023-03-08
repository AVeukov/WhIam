
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.widget.ExpandableListView
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.SIZE_MAX
import ru.veyukov.arseniy.whiam.SIZE_MIN
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.predicate.makeAccessPointsPredicate
import java.security.MessageDigest

@OpenClass
class AccessPointsAdapterData(
    private val accessPointsAdapterGroup: AccessPointsAdapterGroup = AccessPointsAdapterGroup(),
    val wiFiDetails: MutableList<WiFiDetail> = mutableListOf()) {

    fun update(wiFiData: WiFiData, expandableListView: ExpandableListView?) {
        MainContext.INSTANCE.configuration.size = type(calculateChildType())
        val settings = MainContext.INSTANCE.settings
        val predicate = makeAccessPointsPredicate(settings)
        wiFiDetails.clear()
        wiFiDetails.addAll(wiFiData.wiFiDetails(predicate, settings.sortBy(), settings.groupBy()))
        accessPointsAdapterGroup.update(wiFiDetails, expandableListView)
    }

    fun parentsCount(): Int = wiFiDetails.size

    fun parent(index: Int): WiFiDetail = wiFiDetails.getOrNull(index) ?: WiFiDetail.EMPTY

    fun childrenCount(index: Int): Int =
            wiFiDetails.getOrNull(index)?.children?.size ?: 0

    fun child(indexParent: Int, indexChild: Int): WiFiDetail =
            wiFiDetails.getOrNull(indexParent)?.children?.getOrNull(indexChild) ?: WiFiDetail.EMPTY

    fun onGroupCollapsed(groupPosition: Int) =
            accessPointsAdapterGroup.onGroupCollapsed(wiFiDetails, groupPosition)

    fun onGroupExpanded(groupPosition: Int) =
            accessPointsAdapterGroup.onGroupExpanded(wiFiDetails, groupPosition)

    private fun calculateChildType(): Int =
            try {
                with(MessageDigest.getInstance("MD5")) {
                    update(MainContext.INSTANCE.mainActivity.packageName.toByteArray())
                    val digest: ByteArray = digest()
                    digest.contentHashCode()
                }
            } catch (e: Exception) {
                -1
            }

    private fun type(value: Int): Int = SIZE_MAX

}