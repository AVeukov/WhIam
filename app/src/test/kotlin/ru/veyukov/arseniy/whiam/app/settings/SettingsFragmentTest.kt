
package ru.veyukov.arseniy.whiam.settings

import android.os.Build
import androidx.preference.Preference
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.RobolectricUtil
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.settings.SettingsFragment

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class SettingsFragmentTest {

    @Test
    fun testOnCreate() {
        // setup
        val fixture = SettingsFragment()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        // validate
        assertNotNull(fixture.view)
    }

    @Config(sdk = [Build.VERSION_CODES.P])
    @Test
    fun testWiFiOnExitIsVisible() {
        // setup
        val fixture = SettingsFragment()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        val key = fixture.getString(R.string.wifi_off_on_exit_key)
        // execute
        val actual = fixture.findPreference<Preference>(key)
        // validate
        assertTrue(actual!!.isVisible)
    }

    @Test
    fun testWiFiOnExitIsNotVisible() {
        // setup
        val fixture = SettingsFragment()
        RobolectricUtil.INSTANCE.startFragment(fixture)
        val key = fixture.getString(R.string.wifi_off_on_exit_key)
        // execute
        val actual = fixture.findPreference<Preference>(key)
        // validate
        assertFalse(actual!!.isVisible)
    }

}