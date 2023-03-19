package ru.veyukov.arseniy.whiam.scheme.data

data class WifiNodeData (
    val id: Int,
    val sid: String,
    val mac: String,
    val dBm: Int
)