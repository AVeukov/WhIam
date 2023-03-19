package ru.veyukov.arseniy.whiam.scheme.data

data class NodeData (
    val id: Int,
    val xy: ArrayList<Int>,
    val wifi:ArrayList<WifiPointData>
)