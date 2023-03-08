
package ru.veyukov.arseniy.whiam.wifi.predicate

import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.veyukov.arseniy.whiam.wifi.predicate.Predicate
import ru.veyukov.arseniy.whiam.wifi.predicate.ToPredicate
import ru.veyukov.arseniy.whiam.wifi.predicate.makePredicate
import ru.veyukov.arseniy.whiam.wifi.predicate.truePredicate

class MakePredicateTest {

    @Test
    fun testMakePredicateExpectsTruePredicate() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val toPredicate: ToPredicate<TestObject> = { truePredicate }
        val filters: Set<TestObject> = TestObject.values().toSet()
        // execute
        val actual: Predicate = makePredicate(TestObject.values(), filters, toPredicate)
        // validate
        assertTrue(actual(wiFiDetail))
    }

    @Test
    fun testMakePredicateExpectsAnyPredicate() {
        // setup
        val wiFiDetail = WiFiDetail.EMPTY
        val toPredicate: ToPredicate<TestObject> = { truePredicate }
        val filters: Set<TestObject> = setOf(TestObject.VALUE1, TestObject.VALUE3)
        // execute
        val actual: Predicate = makePredicate(TestObject.values(), filters, toPredicate)
        // validate
        assertTrue(actual(wiFiDetail))
    }

}