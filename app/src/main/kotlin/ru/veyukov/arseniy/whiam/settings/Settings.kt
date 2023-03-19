
package ru.veyukov.arseniy.whiam.settings

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.*
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.navigation.NavigationGroup
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.wifi.accesspoint.AccessPointViewType
import ru.veyukov.arseniy.whiam.wifi.band.WiFiBand
import ru.veyukov.arseniy.whiam.wifi.model.GroupBy
import ru.veyukov.arseniy.whiam.wifi.model.Security
import ru.veyukov.arseniy.whiam.wifi.model.SortBy
import ru.veyukov.arseniy.whiam.wifi.model.Strength
import java.util.*

@OpenClass
class Settings(private val repository: Repository) {

    fun initializeDefaultValues(): Unit = repository.initializeDefaultValues()

    fun registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener): Unit =
        repository.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)

    fun scanSpeed(): Int =
        repository.stringAsInteger(
            R.string.scan_speed_key,
            repository.stringAsInteger(R.string.scan_speed_default, SCAN_SPEED_DEFAULT)
        )

    fun cacheOff(): Boolean =
        repository.boolean(R.string.cache_off_key, repository.resourceBoolean(R.bool.cache_off_default))

    fun wiFiBand(wiFiBand: WiFiBand): Unit = repository.save(R.string.wifi_band_key, wiFiBand.ordinal)

    fun countryCode(): String = repository.string(R.string.country_code_key, defaultCountryCode())

    fun languageLocale(): Locale {
        val defaultLanguageTag = defaultLanguageTag()
        val languageTag = repository.string(R.string.language_key, defaultLanguageTag)
        return findByLanguageTag(languageTag)
    }

    fun sortBy(): SortBy = SortBy.STRENGTH// find(SortBy.values(), R.string.sort_by_key, SortBy.STRENGTH)

    fun groupBy(): GroupBy = GroupBy.NONE//find(GroupBy.values(), R.string.group_by_key, GroupBy.NONE)

    fun accessPointView(): AccessPointViewType = find(AccessPointViewType.values(), R.string.ap_view_key, AccessPointViewType.COMPLETE)

//    fun connectionViewType(): ConnectionViewType = find(ConnectionViewType.values(), R.string.connection_view_key, ConnectionViewType.COMPACT)

    fun wiFiBand(): WiFiBand = find(WiFiBand.values(), R.string.wifi_band_key, WiFiBand.GHZ2)
    fun keepScreenOn(): Boolean = repository.boolean(R.string.keep_screen_on_key, repository.resourceBoolean(R.bool.keep_screen_on_default))
    fun wifiDataCollection(): Boolean = repository.boolean(R.string.wifi_data_collection_key, repository.resourceBoolean(R.bool.wifi_data_collection_default))
    fun themeStyle(): ThemeStyle = find(ThemeStyle.values(), R.string.theme_key, ThemeStyle.DARK)
    fun selectedMenu(): NavigationMenu = find(NavigationMenu.values(), R.string.selected_menu_key, NavigationMenu.ACCESS_POINTS)
    fun saveSelectedMenu(navigationMenu: NavigationMenu) {
        if (NavigationGroup.GROUP_FEATURE.navigationMenus.contains(navigationMenu)) {
            repository.save(R.string.selected_menu_key, navigationMenu.ordinal)
        }
    }
    fun findSSIDs(): Set<String> = repository.stringSet(R.string.filter_ssid_key, setOf())
    fun saveSSIDs(values: Set<String>): Unit = repository.saveStringSet(R.string.filter_ssid_key, values)
    fun findWiFiBands(): Set<WiFiBand> = findSet(WiFiBand.values(), R.string.filter_wifi_band_key, WiFiBand.GHZ2)
    fun saveWiFiBands(values: Set<WiFiBand>): Unit = saveSet(R.string.filter_wifi_band_key, values)

    fun findStrengths(): Set<Strength> = setOf(Strength.FOUR, Strength.THREE, Strength.TWO) //findSet(Strength.values(), R.string.filter_strength_key, Strength.FOUR)
    fun saveStrengths(values: Set<Strength>): Unit = saveSet(R.string.filter_strength_key, values)

    fun findSecurities(): Set<Security> = findSet(Security.values(), R.string.filter_security_key, Security.NONE)
    fun saveSecurities(values: Set<Security>): Unit = saveSet(R.string.filter_security_key, values)
    private fun <T : Enum<T>> find(values: Array<T>, key: Int, defaultValue: T): T {
        val value = repository.stringAsInteger(key, defaultValue.ordinal)
        return findOne(values, value, defaultValue)
    }

    private fun <T : Enum<T>> findSet(values: Array<T>, key: Int, defaultValue: T): Set<T> {
        val ordinalDefault = ordinals(values)
        val ordinalSaved = repository.stringSet(key, ordinalDefault)
        return findSet(values, ordinalSaved, defaultValue)
    }

    private fun <T : Enum<T>> saveSet(key: Int, values: Set<T>): Unit = repository.saveStringSet(key, ordinals(values))

    fun minVersionQ(): Boolean = buildMinVersionQ()

    companion object {
        private const val SCAN_SPEED_DEFAULT = 5
        private const val GRAPH_Y_MULTIPLIER = -10
        private const val GRAPH_Y_DEFAULT = 2
    }

}