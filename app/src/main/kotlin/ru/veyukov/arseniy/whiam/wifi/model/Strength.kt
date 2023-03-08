
package ru.veyukov.arseniy.whiam.wifi.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import ru.veyukov.arseniy.whiam.R

enum class Strength(@DrawableRes val imageResource: Int, @ColorRes val colorResource: Int) {
    ZERO(R.drawable.ic_signal_wifi_0_bar, R.color.error),
    ONE(R.drawable.ic_signal_wifi_1_bar, R.color.warning),
    TWO(R.drawable.ic_signal_wifi_2_bar, R.color.warning),
    THREE(R.drawable.ic_signal_wifi_3_bar, R.color.success),
    FOUR(R.drawable.ic_signal_wifi_4_bar, R.color.success);

    fun weak(): Boolean = ZERO == this

    companion object {
        const val colorResourceDefault: Int = R.color.regular

        fun calculate(level: Int): Strength {
            val enumValues: Array<Strength> = enumValues()
            return enumValues[calculateSignalLevel(level, enumValues.size)]
        }

        fun reverse(strength: Strength): Strength {
            val enumValues: Array<Strength> = enumValues()
            return enumValues[enumValues.size - strength.ordinal - 1]
        }
    }

}