package ru.veyukov.arseniy.whiam.scheme

data class WifiPointData (
    val sid: String,
    val mac: String,
    val dBm: Int,
    val dist: Float
)