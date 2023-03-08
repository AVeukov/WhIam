
package ru.veyukov.arseniy.whiam.wifi.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.calculateDistance
import ru.veyukov.arseniy.whiam.wifi.model.calculateSignalLevel
import ru.veyukov.arseniy.whiam.wifi.model.convertIpV4Address
import ru.veyukov.arseniy.whiam.wifi.model.convertSSID
import java.text.DecimalFormat

class WiFiUtilsTest {
    private val decimalFormat = DecimalFormat("#.##")

    @Test
    fun testCalculateDistance() {
        validate(2437, -36, "0.62")
        validate(2437, -42, "1.23")
        validate(2432, -88, "246.34")
        validate(2412, -91, "350.85")
    }

    private fun validate(frequency: Int, level: Int, expected: String) {
        assertEquals(expected, decimalFormat.format(calculateDistance(frequency, level)))
    }

    @Test
    fun testCalculateSignalLevel() {
        assertEquals(0, calculateSignalLevel(-110, 5))
        assertEquals(0, calculateSignalLevel(-89, 5))
        assertEquals(1, calculateSignalLevel(-88, 5))
        assertEquals(1, calculateSignalLevel(-78, 5))
        assertEquals(2, calculateSignalLevel(-77, 5))
        assertEquals(2, calculateSignalLevel(-67, 5))
        assertEquals(3, calculateSignalLevel(-66, 5))
        assertEquals(3, calculateSignalLevel(-56, 5))
        assertEquals(4, calculateSignalLevel(-55, 5))
        assertEquals(4, calculateSignalLevel(0, 5))
    }

    @Test
    fun testConvertIpAddress() {
        assertEquals("21.205.91.7", convertIpV4Address(123456789))
        assertEquals("1.0.0.0", convertIpV4Address(1))
        assertTrue(convertIpV4Address(0).isEmpty())
        assertTrue(convertIpV4Address(-1).isEmpty())
    }

    @Test
    fun testConvertSSID() {
        assertEquals("SSID", convertSSID("\"SSID\""))
        assertEquals("SSID", convertSSID("SSID"))
    }
}