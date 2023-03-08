
package ru.veyukov.arseniy.whiam.util

import java.util.*

val String.Companion.EMPTY: String get() = ""
val String.Companion.SPACE_SEPARATOR: String get() = " "

fun String.specialTrim(): String =
        this.trim { it <= ' ' }.replace(" +".toRegex(), String.SPACE_SEPARATOR)

fun String.toCapitalize(locale: Locale): String =
        this.replaceFirstChar { word -> word.uppercase(locale) }
