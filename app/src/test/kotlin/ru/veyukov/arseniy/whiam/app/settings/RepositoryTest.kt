
package ru.veyukov.arseniy.whiam.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.Resources
import ru.veyukov.arseniy.whiam.R
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.veyukov.arseniy.whiam.settings.Repository

class RepositoryTest {
    private val keyValue = "xyz"
    private val context: Context = mockk()
    private val sharedPreferences: SharedPreferences = mockk()
    private val onSharedPreferenceChangeListener: OnSharedPreferenceChangeListener = mockk()
    private val editor: Editor = mockk()
    private val resources: Resources = mockk()
    private val fixture = spyk(Repository(context))

    @Before
    fun setUp() {
        every { fixture.defaultSharedPreferences(context) } returns sharedPreferences
    }

    @After
    fun tearDown() {
        confirmVerified(resources)
        confirmVerified(context)
        confirmVerified(sharedPreferences)
        confirmVerified(onSharedPreferenceChangeListener)
        confirmVerified(editor)
    }

    @Test
    fun testInitializeDefaultValues() {
        // setup
        every { fixture.defaultValues(context, R.xml.settings, false) } just Runs
        // execute
        fixture.initializeDefaultValues()
        // validate
        verify { fixture.defaultValues(context, R.xml.settings, false) }
    }

    @Test
    fun testSaveString() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = "1111"
        withSave(keyIndex, value)
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(keyIndex, value)
        verifyPreferenceManager()
    }

    @Test
    fun testSaveInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        withSave(keyIndex, value.toString())
        // execute
        fixture.save(keyIndex, value)
        // validate
        verifySave(keyIndex, value.toString())
        verifyPreferenceManager()
    }

    @Test
    fun testString() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = "1111"
        val defaultValue = "2222"
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getString(keyValue, defaultValue) } returns value
        // execute
        val actual = fixture.string(keyIndex, defaultValue)
        // validate
        assertEquals(value, actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getString(keyValue, "" + defaultValue) }
        verifyPreferenceManager()
    }

    @Test
    fun testStringAsInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        val defaultValue = 2222
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getString(keyValue, defaultValue.toString()) } returns value.toString()
        // execute
        val actual = fixture.stringAsInteger(keyIndex, defaultValue)
        // validate
        assertEquals(value, actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getString(keyValue, defaultValue.toString()) }
        verifyPreferenceManager()
    }

    @Test
    fun testStringAsIntegerThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val defaultValue = 2222
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getInt(keyValue, defaultValue) } throws RuntimeException()
        withSave(keyIndex, defaultValue.toString())
        // execute
        val actual = fixture.stringAsInteger(keyIndex, defaultValue)
        // validate
        assertEquals(defaultValue, actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getString(keyValue, defaultValue.toString()) }
        verifySave(keyIndex, defaultValue.toString())
        verifyPreferenceManager()
    }

    @Test
    fun testInteger() {
        // setup
        val keyIndex = R.string.app_full_name
        val value = 1111
        val defaultValue = 2222
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getInt(keyValue, defaultValue) } returns value
        // execute
        val actual = fixture.integer(keyIndex, defaultValue)
        // validate
        assertEquals(value, actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getInt(keyValue, defaultValue) }
        verifyPreferenceManager()
    }

    @Test
    fun testIntegerThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val defaultValue = 2222
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getInt(keyValue, defaultValue) } throws RuntimeException()
        withSave(keyIndex, defaultValue.toString())
        // execute
        val actual = fixture.integer(keyIndex, defaultValue)
        // validate
        assertEquals(defaultValue, actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getInt(keyValue, defaultValue) }
        verifySave(keyIndex, defaultValue.toString())
        verifyPreferenceManager()
    }

    @Test
    fun testResourceBoolean() {
        // setup
        val keyIndex = R.bool.wifi_off_on_exit_default
        every { context.resources } returns resources
        every { resources.getBoolean(keyIndex) } returns true
        // execute
        val actual = fixture.resourceBoolean(keyIndex)
        // validate
        assertTrue(actual)
        verify { context.resources }
        verify { resources.getBoolean(keyIndex) }
    }

    @Test
    fun testBoolean() {
        // setup
        val keyIndex = R.string.app_full_name
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getBoolean(keyValue, false) } returns true
        // execute
        val actual = fixture.boolean(keyIndex, false)
        // validate
        assertTrue(actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getBoolean(keyValue, false) }
        verifyPreferenceManager()
    }

    @Test
    fun testBooleanThrowsException() {
        // setup
        val defaultValue = true
        val keyIndex = R.string.app_full_name
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getBoolean(keyValue, defaultValue) } throws RuntimeException()
        withSave(keyIndex, defaultValue)
        // execute
        val actual = fixture.boolean(keyIndex, defaultValue)
        // validate
        assertTrue(actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getBoolean(keyValue, defaultValue) }
        verifySave(keyIndex, defaultValue)
        verifyPreferenceManager()
    }

    @Test
    fun testRegisterOnSharedPreferenceChangeListener() {
        // setup
        every { sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener) } just runs
        // execute
        fixture.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
        // verify
        verify { sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener) }
        verifyPreferenceManager()
    }

    @Test
    fun testStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val expected = setOf("123")
        val defaultValues = setOf("567")
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.getStringSet(keyValue, defaultValues) } returns expected
        // execute
        val actual = fixture.stringSet(keyIndex, defaultValues)
        // validate
        assertEquals(expected, actual)
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.getStringSet(keyValue, defaultValues) }
        verifyPreferenceManager()
    }

    @Test
    fun testStringSetThrowsException() {
        // setup
        val keyIndex = R.string.app_full_name
        val expected = setOf("567")
        every { sharedPreferences.getStringSet(keyValue, expected) } throws RuntimeException()
        withSave(keyIndex, expected)
        // execute
        val actual = fixture.stringSet(keyIndex, expected)
        // validate
        assertEquals(expected, actual)
        verify { sharedPreferences.getStringSet(keyValue, expected) }
        verifySave(keyIndex, expected)
        verifyPreferenceManager()
    }

    @Test
    fun testSaveStringSet() {
        // setup
        val keyIndex = R.string.app_full_name
        val values = setOf("123")
        withSave(keyIndex, values)
        // execute
        fixture.saveStringSet(keyIndex, values)
        // validate
        verifySave(keyIndex, values)
        verifyPreferenceManager()
    }

    private fun verifySave(keyIndex: Int, values: Set<String>) {
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.edit() }
        verify { editor.putStringSet(keyValue, values) }
        verify { editor.apply() }
    }

    private fun verifySave(keyIndex: Int, value: String) {
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.edit() }
        verify { editor.putString(keyValue, value) }
        verify { editor.apply() }
    }

    private fun verifySave(keyIndex: Int, value: Boolean) {
        verify { context.getString(keyIndex) }
        verify { sharedPreferences.edit() }
        verify { editor.putBoolean(keyValue, value) }
        verify { editor.apply() }
    }

    private fun withSave(keyIndex: Int, value: String) {
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(keyValue, value) } returns editor
        every { editor.apply() } just runs
    }

    private fun withSave(keyIndex: Int, value: Boolean) {
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(keyValue, value) } returns editor
        every { editor.apply() } just runs
    }

    private fun withSave(keyIndex: Int, value: Set<String>) {
        every { context.getString(keyIndex) } returns keyValue
        every { sharedPreferences.edit() } returns editor
        every { editor.putStringSet(keyValue, value) } returns editor
        every { editor.apply() } just runs
    }

    private fun verifyPreferenceManager() {
        verify(atLeast = 1) { fixture.defaultSharedPreferences(context) }
    }

}