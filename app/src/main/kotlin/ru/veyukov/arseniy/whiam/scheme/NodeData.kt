package ru.veyukov.arseniy.whiam.scheme

data class NodeData (
    val id: Int,
    val xy: List<Int>,
    val wifi: List<WifiPointData>
)