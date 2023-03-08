
package ru.veyukov.arseniy.whiam

import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.settings.ThemeStyle
import java.util.*

class MainReload(settings: Settings) {
    var themeStyle: ThemeStyle
        private set
    var languageLocale: Locale
        private set

    fun shouldReload(settings: Settings): Boolean =
            themeChanged(settings) || languageChanged(settings)


    private fun themeChanged(settings: Settings): Boolean {
        val settingThemeStyle = settings.themeStyle()
        val themeChanged = themeStyle != settingThemeStyle
        if (themeChanged) {
            themeStyle = settingThemeStyle
        }
        return themeChanged
    }

    private fun languageChanged(settings: Settings): Boolean {
        val settingLanguageLocale = settings.languageLocale()
        val languageLocaleChanged = languageLocale != settingLanguageLocale
        if (languageLocaleChanged) {
            languageLocale = settingLanguageLocale
        }
        return languageLocaleChanged
    }

    init {
        themeStyle = settings.themeStyle()
        languageLocale = settings.languageLocale()
    }
}