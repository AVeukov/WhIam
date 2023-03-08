
package ru.veyukov.arseniy.whiam.app

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal class ChildAtPosition(private val parentMatcher: Matcher<View>, private val position: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
    }

    public override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return (parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(position))
    }
}

private const val SLEEP_TIME_SHORT = 5000
private const val SLEEP_TIME_LONG = SLEEP_TIME_SHORT * 3

internal fun pressBackButton() {
    pauseShort()
    Espresso.pressBack()
}

internal fun pauseShort() {
    pause(SLEEP_TIME_SHORT)
}

internal fun pauseLong() {
    pause(SLEEP_TIME_LONG)
}

private fun pause(sleepTime: Int) {
    try {
        Thread.sleep(sleepTime.toLong())
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}
