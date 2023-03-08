
package ru.veyukov.arseniy.whiam.settings

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.veyukov.arseniy.whiam.RobolectricUtil
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannelCountry.Companion.findAll
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.settings.CountryPreference
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class CountryPreferenceTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val countries = findAll()
    private val fixture = CountryPreference(mainActivity, Robolectric.buildAttributeSet().build())
    private val currentLocale = Locale.getDefault()

    @Test
    fun testEntries() {
        // execute
        val actual: Array<CharSequence> = fixture.entries
        // validate
        assertEquals(countries.size, actual.size)
        countries.forEach {
            val countryName: String = it.countryName(currentLocale)
            assertTrue(countryName, actual.contains(countryName))
        }
    }

    @Test
    fun testEntryValues() {
        // execute
        val actual: Array<CharSequence> = fixture.entryValues
        // validate
        assertEquals(countries.size, actual.size)
        countries.forEach {
            val countryCode: String = it.countryCode()
            assertTrue(countryCode, actual.contains(countryCode))
        }
    }
}