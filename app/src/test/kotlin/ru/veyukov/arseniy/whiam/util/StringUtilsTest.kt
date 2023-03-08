
package ru.veyukov.arseniy.whiam.util

import org.junit.Assert.assertEquals
import org.junit.Test

class StringUtilsTest {

    @Test
    fun testSpecialTrim() {
        // setup
        val expected = "ABS ADF"
        val value = "    ABS    ADF    "
        // execute
        val actual: String = value.specialTrim()
        // verify
        assertEquals(expected, actual)
    }

}