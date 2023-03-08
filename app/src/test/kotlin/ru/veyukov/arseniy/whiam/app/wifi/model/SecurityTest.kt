
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.wifi.model.Security.Companion.findAll
import ru.veyukov.arseniy.whiam.wifi.model.Security.Companion.findOne
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.model.Security

class SecurityTest {
    @Test
    fun testSecurity() {
        assertEquals(6, Security.values().size)
    }

    @Test
    fun testImageResource() {
        assertEquals(R.drawable.ic_lock_open, Security.NONE.imageResource)
        assertEquals(R.drawable.ic_lock_outline, Security.WPS.imageResource)
        assertEquals(R.drawable.ic_lock_outline, Security.WEP.imageResource)
        assertEquals(R.drawable.ic_lock, Security.WPA.imageResource)
        assertEquals(R.drawable.ic_lock, Security.WPA2.imageResource)
        assertEquals(R.drawable.ic_lock, Security.WPA3.imageResource)
    }

    @Test
    fun testFindAll() {
        // setup
        val capabilities = "WPA-WPA2-WPA-WEP-YZX-WPA3-WPS-WPA2"
        val expected: Set<Security> = setOf(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3)
        // execute
        val actual: Set<Security> = findAll(capabilities)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testFindAllWithAdditional() {
        // setup
        val capabilities = "WPA-[FT/WPA2]-[WPA]-[WEP-FT/SAE+TST][KPG-WPS]"
        val expected: Set<Security> = setOf(Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3)
        // execute
        val actual: Set<Security> = findAll(capabilities)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testFindAllNoneFound() {
        // setup
        val capabilities = "ESS"
        val expected: Set<Security> = setOf(Security.NONE)
        // execute
        val actual: Set<Security> = findAll(capabilities)
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testFindOne() {
        assertEquals(Security.NONE, findOne("xyz"))
        assertEquals(Security.NONE, findOne("ESS"))
        assertEquals(Security.NONE, findOne("WPA3-WPA2+WPA[ESS]WEP[]WPS-NONE"))
        assertEquals(Security.WPS, findOne("WPA3-WPA2+WPA[ESS]WEP[]WPS"))
        assertEquals(Security.WEP, findOne("WPA3-WPA2+WPA[ESS]WEP[]"))
        assertEquals(Security.WPA, findOne("WPA3-WPA2+WPA[ESS]"))
        assertEquals(Security.WPA2, findOne("WPA3-WPA2+[ESS]"))
        assertEquals(Security.WPA3, findOne("WPA3+[ESS]"))
        assertEquals(Security.WPA3, findOne("[FT/SAE]+ESS"))
    }

    @Test
    fun testOrder() {
        val expected: Array<Security> = arrayOf(Security.NONE, Security.WPS, Security.WEP, Security.WPA, Security.WPA2, Security.WPA3)
        assertArrayEquals(expected, Security.values())
    }
}