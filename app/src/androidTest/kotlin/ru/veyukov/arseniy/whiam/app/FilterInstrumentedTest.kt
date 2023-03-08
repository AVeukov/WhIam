
package ru.veyukov.arseniy.whiam.app

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers

internal class FilterInstrumentedTest : Runnable {
    override fun run() {
        actionOpen()
        actionClose()
    }

    private fun actionClose() {
        pauseLong()
        Espresso.onView(
                Matchers.allOf(
                        ViewMatchers.withId(android.R.id.button3),
                        ViewMatchers.withText(FILTER_CLOSE_TAG),
                        ChildAtPosition(
                                ChildAtPosition(
                                        ViewMatchers.withClassName(Matchers.`is`("android.widget.ScrollView")),
                                        FILTER_BUTTON_CLOSE), FILTER_ACTION)))
                .perform(ViewActions.scrollTo(), ViewActions.click())
    }

    private fun actionOpen() {
        pauseShort()
        Espresso.onView(
                Matchers.allOf(
                        ViewMatchers.withId(R.id.action_filter),
                        ViewMatchers.withContentDescription(FILTER_BUTTON_TAG),
                        ChildAtPosition(
                                ChildAtPosition(
                                        ViewMatchers.withId(R.id.toolbar),
                                        FILTER_BUTTON_OPEN),
                                FILTER_ACTION),
                        ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }


    companion object {
        private const val FILTER_BUTTON_OPEN = 2
        private const val FILTER_BUTTON_CLOSE = 0
        private const val FILTER_ACTION = 0
        private const val FILTER_BUTTON_TAG = "Filter"
        private const val FILTER_CLOSE_TAG = "Close"
    }
}