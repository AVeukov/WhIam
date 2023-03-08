

package ru.veyukov.arseniy.whiam.util

import android.os.Build

fun buildMinVersionT(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun buildMinVersionS(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

fun buildMinVersionR(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

fun buildMinVersionQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun buildMinVersionP(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun buildVersionP(): Boolean = Build.VERSION.SDK_INT == Build.VERSION_CODES.P

fun buildMinVersionN(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
