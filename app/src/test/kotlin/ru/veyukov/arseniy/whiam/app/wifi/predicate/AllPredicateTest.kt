
package ru.veyukov.arseniy.whiam.wifi.predicate

import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.predicate.allPredicate
import ru.veyukov.arseniy.whiam.wifi.predicate.falsePredicate
import ru.veyukov.arseniy.whiam.wifi.predicate.truePredicate

class AllPredicateTest {

    @Test
    fun testAllPredicateIsTrue() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val fixture = listOf(truePredicate, truePredicate, truePredicate).allPredicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAllPredicateIsFalse() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val fixture = listOf(falsePredicate, truePredicate, falsePredicate).allPredicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertFalse(actual)
    }

}