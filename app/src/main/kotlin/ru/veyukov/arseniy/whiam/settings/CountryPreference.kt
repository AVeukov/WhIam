
package ru.veyukov.arseniy.whiam.settings

import android.content.Context
import android.util.AttributeSet
import ru.veyukov.arseniy.whiam.util.defaultCountryCode
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannelCountry
import java.util.*

private fun data(): List<Data> {
    val currentLocale: Locale = MainContext.INSTANCE.settings.languageLocale()
    return WiFiChannelCountry.findAll()
            .map { Data(it.countryCode(), it.countryName(currentLocale)) }
            .sorted()
}

class CountryPreference(context: Context, attrs: AttributeSet) :
        CustomPreference(context, attrs, data(), defaultCountryCode())
