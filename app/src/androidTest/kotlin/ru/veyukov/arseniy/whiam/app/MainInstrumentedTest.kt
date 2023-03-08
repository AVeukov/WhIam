
package ru.veyukov.arseniy.whiam.app

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.veyukov.arseniy.whiam.MainActivity

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {

    @get:Rule
    val activityTestRule: ActivityScenarioRule<MainActivity> = activityScenarioRule()

    @Test
    fun testNavigation() {
        pauseShort()
        NavigationInstrumentedTest().run()
        pauseShort()
    }

    @Test
    fun testScanner() {
        pauseShort()
        ScannerInstrumentedTest().run()
        pauseShort()
    }

    @Test
    fun testFilter() {
        pauseShort()
        FilterInstrumentedTest().run()
        pauseShort()
    }
}