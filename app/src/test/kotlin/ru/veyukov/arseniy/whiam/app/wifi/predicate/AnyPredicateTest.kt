
package ru.veyukov.arseniy.whiam.wifi.predicate

import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.predicate.anyPredicate
import ru.veyukov.arseniy.whiam.wifi.predicate.falsePredicate
import ru.veyukov.arseniy.whiam.wifi.predicate.truePredicate

class AmyPredicateTest {

    @Test
    fun testAnyPredicateIsTrue() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val fixture = listOf(falsePredicate, truePredicate, falsePredicate).anyPredicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAnyPredicateIsFalse() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val fixture = listOf(falsePredicate, falsePredicate, falsePredicate).anyPredicate()
        // execute
        val actual = fixture(wiFiDetail)
        // validate
        assertFalse(actual)
    }

}