
package ru.veyukov.arseniy.whiam.wifi.band

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.band.WiFiChannelCountryGHZ5

class WiFiChannelCountryGHZ5Test {
    private val channelsSet1: Set<Int> = setOf(36, 40, 44, 48, 52, 56, 60, 64)
    private val channelsSet2: Set<Int> = setOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)
    private val channelsSet3: Set<Int> = setOf(149, 153, 157, 161, 165)
    private val fixture = WiFiChannelCountryGHZ5()

    @Test
    fun testChannelsAustraliaCanada() {
        val expected = channelsSet1.union(setOf(100, 104, 108, 112, 116, 132, 136, 140, 144)).union(channelsSet3)
        listOf("AU", "CA").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(it, expected.size, actual.size)
            assertEquals(it, expected, actual)
        }
    }

    @Test
    fun testChannelsChinaSouthKorea() {
        val expected = channelsSet1.union(channelsSet3)
        listOf("CN", "KR").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(it, expected.size, actual.size)
            assertEquals(it, expected, actual)
        }
    }

    @Test
    fun testChannelsJapanTurkeySouthAfrica() {
        val expected = channelsSet1.union(channelsSet2)
        listOf("JP", "TR", "ZA").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(it, expected.size, actual.size)
            assertEquals(it, expected, actual)
        }
    }

    @Test
    fun testChannelsRussia() {
        val expected = channelsSet1.union(setOf(132, 136, 140, 144)).union(channelsSet3)
        val actual = fixture.findChannels("RU")
        assertEquals(expected.size, actual.size)
        assertEquals(expected, actual)
    }

    @Test
    fun testChannelsUS() {
        val expected = channelsSet1.union(channelsSet2).union(channelsSet3).union(setOf(169, 173, 177))
        val actual = fixture.findChannels("US")
        assertEquals(expected.size, actual.size)
        assertEquals(expected, actual)
    }

    @Test
    fun testChannelsETSI() {
        val countriesETSI = listOf(
                "AT",      // ETSI Austria
                "BE",      // ETSI Belgium
                "CH",      // ETSI Switzerland
                "CY",      // ETSI Cyprus
                "CZ",      // ETSI Czechia
                "DE",      // ETSI Germany
                "DK",      // ETSI Denmark
                "EE",      // ETSI Estonia
                "ES",      // ETSI Spain
                "FI",      // ETSI Finland
                "FR",      // ETSI France
                "GR",      // ETSI Greece
                "HU",      // ETSI Hungary
                "IE",      // ETSI Ireland
                "IS",      // ETSI Iceland
                "IT",      // ETSI Italy
                "LI",      // ETSI Liechtenstein
                "LT",      // ETSI Lithuania
                "LU",      // ETSI Luxembourg
                "LV",      // ETSI Latvia
                "MT",      // ETSI Malta
                "NL",      // ETSI Netherlands
                "NO",      // ETSI Norway
                "PL",      // ETSI Poland
                "PT",      // ETSI Portugal
                "RO",      // ETSI Romania
                "SE",      // ETSI Sweden
                "SI",      // ETSI Slovenia
                "SK",      // ETSI Slovakia
                "IL"       // ETSI Israel
        )

        val expected = channelsSet1
                .union(setOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144))
                .union(setOf(149, 153, 157, 161, 165, 169, 173))
        countriesETSI.forEach {
            val actual = fixture.findChannels(it)
            assertEquals(it, expected.size, actual.size)
            assertEquals(it, expected, actual)
        }
    }

    @Test
    fun testChannelsOther() {
        val expected = channelsSet1.union(channelsSet2).union(channelsSet3)
        listOf("UK", "BR", "XYZ").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(it, expected.size, actual.size)
            assertEquals(it, expected, actual)
        }
    }

}