
package ru.veyukov.arseniy.whiam.settings

import io.mockk.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import ru.veyukov.arseniy.whiam.settings.Repository
import ru.veyukov.arseniy.whiam.settings.Settings

class SettingsAndroidQTest {
    private val repository: Repository = mockk()
    private val fixture = spyk(Settings(repository))

    @Before
    fun setUp() {
        every { fixture.minVersionQ() } returns true
    }

    @After
    fun tearDown() {
        confirmVerified(repository)
    }

    @Test
    fun testWiFiOffOnExitAndroidQ() {
        // execute
        val actual = fixture.wiFiOffOnExit()
        // validate
        assertFalse(actual)
        verify { fixture.minVersionQ() }
    }

}