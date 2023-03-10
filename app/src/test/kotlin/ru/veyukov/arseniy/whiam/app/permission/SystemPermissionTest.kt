
package ru.veyukov.arseniy.whiam.permission

import android.app.Activity
import android.location.LocationManager
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.permission.SystemPermission

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class SystemPermissionTest {
    private val activity: Activity = mock()
    private val locationManager: LocationManager = mock()
    private val fixture: SystemPermission = SystemPermission(activity)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(locationManager)
    }

    @Test
    fun testEnabledWhenGPSProviderIsEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabledWhenLocationEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
    }

    @Test
    fun testEnabledWhenNetworkProviderEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(true)
        // execute
        val actual = fixture.enabled()
        // validate
        assertTrue(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @Test
    fun testEnabledWhenAllProvidersAreDisabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenReturn(false)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabledWhenAllProvidersThrowException() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenReturn(locationManager)
        whenever(locationManager.isLocationEnabled).thenThrow(RuntimeException::class.java)
        whenever(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).thenThrow(RuntimeException::class.java)
        whenever(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenThrow(RuntimeException::class.java)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(LocationManager::class.java)
        verify(locationManager).isLocationEnabled
        verify(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        verify(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun testEnabled() {
        // setup
        whenever(activity.getSystemService(LocationManager::class.java)).thenThrow(RuntimeException::class.java)
        // execute
        val actual = fixture.enabled()
        // validate
        assertFalse(actual)
        verify(activity).getSystemService(LocationManager::class.java)
    }
}