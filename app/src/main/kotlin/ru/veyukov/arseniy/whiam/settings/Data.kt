
package ru.veyukov.arseniy.whiam.settings

class Data(val code: String, val name: String) : Comparable<Data> {

    override fun compareTo(other: Data): Int =
            compareBy<Data> { it.name }.thenBy { it.code }.compare(this, other)

}