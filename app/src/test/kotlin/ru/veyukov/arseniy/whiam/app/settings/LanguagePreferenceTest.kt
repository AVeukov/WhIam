
package ru.veyukov.arseniy.whiam.settings

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.veyukov.arseniy.whiam.util.supportedLanguages
import ru.veyukov.arseniy.whiam.util.toCapitalize
import ru.veyukov.arseniy.whiam.util.toLanguageTag
import ru.veyukov.arseniy.whiam.RobolectricUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.settings.LanguagePreference
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class LanguagePreferenceTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val languages = supportedLanguages()
    private val fixture = LanguagePreference(mainActivity, Robolectric.buildAttributeSet().build())

    @Test
    fun testEntries() {
        // execute
        val actual: Array<CharSequence> = fixture.entries
        // validate
        assertEquals(languages.size, actual.size)
        languages.forEach {
            val displayName: String = it.getDisplayName(it).toCapitalize(Locale.getDefault())
            assertTrue(displayName, actual.contains(displayName))
        }
    }

    @Test
    fun testEntryValues() {
        // execute
        val actual: Array<CharSequence> = fixture.entryValues
        // validate
        assertEquals(languages.size, actual.size)
        languages.forEach {
            val languageTag: String = toLanguageTag(it)
            assertTrue(languageTag, actual.contains(languageTag))
        }
    }
}