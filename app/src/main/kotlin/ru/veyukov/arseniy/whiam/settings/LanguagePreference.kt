
package ru.veyukov.arseniy.whiam.settings

import android.content.Context
import android.util.AttributeSet
import ru.veyukov.arseniy.whiam.util.defaultLanguageTag
import ru.veyukov.arseniy.whiam.util.supportedLanguages
import ru.veyukov.arseniy.whiam.util.toCapitalize
import ru.veyukov.arseniy.whiam.util.toLanguageTag
import java.util.*

private fun data(): List<Data> = supportedLanguages()
        .map { map(it) }
        .sorted()

private fun map(it: Locale): Data =
        Data(toLanguageTag(it), it.getDisplayName(it).toCapitalize(Locale.getDefault()))

class LanguagePreference(context: Context, attrs: AttributeSet) :
        CustomPreference(context, attrs, data(), defaultLanguageTag())