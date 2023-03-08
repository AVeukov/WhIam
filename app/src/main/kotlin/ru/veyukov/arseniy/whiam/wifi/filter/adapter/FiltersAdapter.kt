
package ru.veyukov.arseniy.whiam.wifi.filter.adapter

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.settings.Settings
import java.io.Serializable

@OpenClass
class FiltersAdapter(private val settings: Settings) {
    private var ssidAdapter: SSIDAdapter = SSIDAdapter(settings.findSSIDs())
    private var wiFiBandAdapter: WiFiBandAdapter = WiFiBandAdapter(settings.findWiFiBands())
    private var strengthAdapter: StrengthAdapter = StrengthAdapter(settings.findStrengths())
    private var securityAdapter: SecurityAdapter = SecurityAdapter(settings.findSecurities())

    fun reload() {
        ssidAdapter = SSIDAdapter(settings.findSSIDs())
        wiFiBandAdapter = WiFiBandAdapter(settings.findWiFiBands())
        strengthAdapter = StrengthAdapter(settings.findStrengths())
        securityAdapter = SecurityAdapter(settings.findSecurities())
    }

    fun reset(): Unit =
        filterAdapters(isAccessPoints()).forEach {
            it.reset()
            it.save(settings)
        }

    fun save(): Unit = filterAdapters(isAccessPoints()).forEach { it.save(settings) }

    fun ssidAdapter(): SSIDAdapter = ssidAdapter

    fun wiFiBandAdapter(): WiFiBandAdapter = wiFiBandAdapter

    fun strengthAdapter(): StrengthAdapter = strengthAdapter

    fun securityAdapter(): SecurityAdapter = securityAdapter

    internal fun isActive(): Boolean = filterAdapters(isAccessPoints()).any { it.isActive() }

    internal fun filterAdapters(accessPoints: Boolean): List<BasicFilterAdapter<out Serializable>> =
        if (accessPoints)
            listOf(ssidAdapter, strengthAdapter, securityAdapter, wiFiBandAdapter)
        else
            listOf(ssidAdapter, strengthAdapter, securityAdapter)

    private fun isAccessPoints(): Boolean =
        NavigationMenu.ACCESS_POINTS == MainContext.INSTANCE.mainActivity.currentNavigationMenu()
}
