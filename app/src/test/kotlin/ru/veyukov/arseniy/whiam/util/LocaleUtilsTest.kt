
package ru.veyukov.arseniy.whiam.util

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class LocaleUtilsTest {
    @Test
    fun testAllCountries() {
        // execute
        val actual = allCountries()
        // validate
        assertTrue(actual.size >= 2)
        assertTrue(actual[0].country < actual[actual.size - 1].country)
    }

    @Test
    fun testFindByCountryCode() {
        // setup
        val expected = allCountries()[0]
        // execute
        val actual = findByCountryCode(expected.country)
        // validate
        assertEquals(expected, actual)
        assertEquals(expected.country, actual.country)
        assertEquals(expected.displayCountry, actual.displayCountry)
        assertNotEquals(expected.country, expected.displayCountry)
        assertNotEquals(actual.country, actual.displayCountry)
    }

    @Test
    fun testFindByCountryCodeWithUnknownCode() {
        // execute
        val actual = findByCountryCode("WW")
        // validate
        assertEquals(Locale.getDefault(), actual)
    }

    @Test
    fun testToLanguageTag() {
        assertEquals(Locale.US.language + "_" + Locale.US.country, toLanguageTag(Locale.US))
        assertEquals(Locale.ENGLISH.language + "_", toLanguageTag(Locale.ENGLISH))
    }

    @Test
    fun testFindByLanguageTagWithUnknownTag() {
        val defaultLocal = Locale.getDefault()
        assertEquals(defaultLocal, findByLanguageTag(String.EMPTY))
        assertEquals(defaultLocal, findByLanguageTag("WW"))
        assertEquals(defaultLocal, findByLanguageTag("WW_HH_TT"))
    }

    @Test
    fun testFindByLanguageTag() {
        assertEquals(Locale.SIMPLIFIED_CHINESE, findByLanguageTag(toLanguageTag(Locale.SIMPLIFIED_CHINESE)))
        assertEquals(Locale.TRADITIONAL_CHINESE, findByLanguageTag(toLanguageTag(Locale.TRADITIONAL_CHINESE)))
        assertEquals(Locale.ENGLISH, findByLanguageTag(toLanguageTag(Locale.ENGLISH)))
    }

    @Test
    fun testSupportedLanguages() {
        // setup
        val expected: Set<Locale> = setOf(
                BULGARIAN,
                Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE,
                Locale.ENGLISH,
                Locale.FRENCH,
                Locale.GERMAN,
                Locale.ITALIAN,
                Locale.JAPANESE,
                POLISH,
                PORTUGUESE,
                SPANISH,
                RUSSIAN,
                UKRAINIAN,
                Locale.getDefault())
        // execute
        val actual = supportedLanguages()
        // validate
        assertEquals(expected.size, actual.size)
        for (locale in expected) {
            assertTrue(actual.contains(locale))
        }
    }

    @Test
    fun testDefaultCountryCode() {
        assertEquals(Locale.getDefault().country, defaultCountryCode())
    }

    @Test
    fun testDefaultLanguageTag() {
        assertEquals(toLanguageTag(Locale.getDefault()), defaultLanguageTag())
    }
}