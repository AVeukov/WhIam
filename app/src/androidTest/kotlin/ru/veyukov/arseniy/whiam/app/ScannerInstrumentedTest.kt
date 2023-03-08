
package ru.veyukov.arseniy.whiam.app

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

internal class ScannerInstrumentedTest : Runnable {
    override fun run() {
        scannerAction(SCANNER_PAUSE_TAG)
        pauseLong()
        scannerAction(SCANNER_RESUME_TAG)
    }

    private fun scannerAction(tag: String) {
        pauseShort()
        val actionMenuItemView = Espresso.onView(
                Matchers.allOf(
                        ViewMatchers.withId(R.id.action_scanner),
                        ViewMatchers.withContentDescription(tag),
                        ChildAtPosition(
                                ChildAtPosition(ViewMatchers.withId(R.id.toolbar), SCANNER_BUTTON),
                                SCANNER_ACTION),
                        ViewMatchers.isDisplayed()))
        actionMenuItemView.perform(ViewActions.click())
    }

    companion object {
        private const val SCANNER_BUTTON = 2
        private const val SCANNER_ACTION = 1
        private const val SCANNER_PAUSE_TAG = "Pause"
        private const val SCANNER_RESUME_TAG = "Play"
    }
}