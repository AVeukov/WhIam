
package ru.veyukov.arseniy.whiam.wifi.filter

import android.app.AlertDialog
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.filter.adapter.StrengthAdapter
import ru.veyukov.arseniy.whiam.wifi.model.Strength

internal class StrengthFilter(strengthAdapter: StrengthAdapter, alertDialog: AlertDialog) :
        EnumFilter<Strength, StrengthAdapter>(
                mapOf(
                        Strength.ZERO to R.id.filterStrength0,
                        Strength.ONE to R.id.filterStrength1,
                        Strength.TWO to R.id.filterStrength2,
                        Strength.THREE to R.id.filterStrength3,
                        Strength.FOUR to R.id.filterStrength4
                ),
                strengthAdapter,
                alertDialog,
                R.id.filterStrength
        )