
package ru.veyukov.arseniy.whiam.wifi.model

import androidx.annotation.DrawableRes
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.R
import java.util.*

private const val SAE = "SAE"

enum class Security(@DrawableRes val imageResource: Int, val additional: String = String.EMPTY) {
    NONE(R.drawable.ic_lock_open),
    WPS(R.drawable.ic_lock_outline),
    WEP(R.drawable.ic_lock_outline),
    WPA(R.drawable.ic_lock),
    WPA2(R.drawable.ic_lock),
    WPA3(R.drawable.ic_lock, SAE);

    companion object {
        private val regex = Regex("[^A-Z0-9]")

        fun findAll(capabilities: String): Set<Security> =
                parse(capabilities).mapNotNull(transform()).toSortedSet().ifEmpty { setOf(NONE) }

        fun findOne(capabilities: String): Security = findAll(capabilities).first()

        private fun transform(): (String) -> Security? = {
            try {
                enumValueOf<Security>(it)
            } catch (e: IllegalArgumentException) {
                enumValues<Security>().find { security -> security.additional == it }
            }
        }

        private fun parse(capabilities: String): List<String> =
                regex.replace(capabilities.uppercase(Locale.getDefault()), "-")
                        .split("-")
                        .filter { it.isNotBlank() }
    }

}
