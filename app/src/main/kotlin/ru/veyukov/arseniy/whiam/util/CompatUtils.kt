
package ru.veyukov.arseniy.whiam.util

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.res.Configuration
import android.content.res.Resources
import android.net.wifi.ScanResult
import android.os.Build
import java.util.*

fun Context.createContext(newLocale: Locale): Context =
    if (buildMinVersionN()) {
        createContextAndroidN(newLocale)
    } else {
        createContextLegacy(newLocale)
    }

@TargetApi(Build.VERSION_CODES.N)
private fun Context.createContextAndroidN(newLocale: Locale): Context {
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(newLocale)
    return createConfigurationContext(configuration)
}

@Suppress("DEPRECATION")
private fun Context.createContextLegacy(newLocale: Locale): Context {
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.locale = newLocale
    resources.updateConfiguration(configuration, resources.displayMetrics)
    return this
}

fun Context.packageInfo(): PackageInfo =
    if (buildMinVersionT()) {
        packageInfoAndroidT()
    } else {
        packageInfoLegacy()
    }

@TargetApi(Build.VERSION_CODES.TIRAMISU)
private fun Context.packageInfoAndroidT(): PackageInfo =
    packageManager.getPackageInfo(packageName, PackageInfoFlags.of(0))

@Suppress("DEPRECATION")
private fun Context.packageInfoLegacy(): PackageInfo =
    packageManager.getPackageInfo(packageName, 0)

fun ScanResult.ssid(): String =
    if (buildMinVersionT()) {
        ssidAndroidT()
    } else {
        ssidLegacy()
    }.removeSurrounding("\"")

@TargetApi(Build.VERSION_CODES.TIRAMISU)
private fun ScanResult.ssidAndroidT(): String =
    wifiSsid?.toString() ?: String.EMPTY

@Suppress("DEPRECATION")
private fun ScanResult.ssidLegacy(): String =
    if (SSID == null) String.EMPTY else SSID
