
package ru.veyukov.arseniy.whiam.permission

import android.annotation.TargetApi
import android.app.Activity
import android.location.LocationManager
import android.os.Build
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.buildMinVersionP

@OpenClass
class SystemPermission(private val activity: Activity) {
    fun enabled(): Boolean =
        try {
            val locationManager = activity.getSystemService(LocationManager::class.java)
            locationEnabled(locationManager) || networkProviderEnabled(locationManager) || gpsProviderEnabled(
                locationManager
            )
        } catch (e: Exception) {
            false
        }

    private fun gpsProviderEnabled(locationManager: LocationManager): Boolean =
        try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }

    private fun networkProviderEnabled(locationManager: LocationManager): Boolean =
        try {
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            false
        }

    private fun locationEnabled(locationManager: LocationManager): Boolean =
        buildMinVersionP() && locationEnabledAndroidP(locationManager)

    @TargetApi(Build.VERSION_CODES.P)
    private fun locationEnabledAndroidP(locationManager: LocationManager): Boolean =
        try {
            locationManager.isLocationEnabled
        } catch (e: Exception) {
            false
        }

}