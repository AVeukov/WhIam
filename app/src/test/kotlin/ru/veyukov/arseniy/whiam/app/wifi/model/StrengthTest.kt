
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.model.Strength.Companion.calculate
import ru.veyukov.arseniy.whiam.wifi.model.Strength.Companion.reverse
import org.junit.Assert.*
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.Strength

class StrengthTest {
    @Test
    fun testStrength() {
        assertEquals(5, Strength.values().size)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_signal_wifi_0_bar, Strength.ZERO.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_1_bar, Strength.ONE.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_2_bar, Strength.TWO.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_3_bar, Strength.THREE.imageResource)
        assertEquals(R.drawable.ic_signal_wifi_4_bar, Strength.FOUR.imageResource)
    }

    @Test
    fun testColorResource() {
        assertEquals(R.color.error, Strength.ZERO.colorResource)
        assertEquals(R.color.warning, Strength.ONE.colorResource)
        assertEquals(R.color.warning, Strength.TWO.colorResource)
        assertEquals(R.color.success, Strength.THREE.colorResource)
        assertEquals(R.color.success, Strength.FOUR.colorResource)
    }

    @Test
    fun testWeak() {
        assertTrue(Strength.ZERO.weak())
        assertFalse(Strength.ONE.weak())
        assertFalse(Strength.TWO.weak())
        assertFalse(Strength.THREE.weak())
        assertFalse(Strength.FOUR.weak())
    }

    @Test
    fun testCalculate() {
        assertEquals(Strength.ZERO, calculate(-89))
        assertEquals(Strength.ONE, calculate(-88))
        assertEquals(Strength.ONE, calculate(-78))
        assertEquals(Strength.TWO, calculate(-77))
        assertEquals(Strength.TWO, calculate(-67))
        assertEquals(Strength.THREE, calculate(-66))
        assertEquals(Strength.THREE, calculate(-56))
        assertEquals(Strength.FOUR, calculate(-55))
        assertEquals(Strength.FOUR, calculate(0))
    }

    @Test
    fun testReverse() {
        assertEquals(Strength.FOUR, reverse(Strength.ZERO))
        assertEquals(Strength.THREE, reverse(Strength.ONE))
        assertEquals(Strength.TWO, reverse(Strength.TWO))
        assertEquals(Strength.ONE, reverse(Strength.THREE))
        assertEquals(Strength.ZERO, reverse(Strength.FOUR))
    }
}