package ru.veyukov.arseniy.whiam.scheme.data

import ru.veyukov.arseniy.whiam.scheme.data.BuildingData

data class SchemeData (
    val nameScheme: String,
    val scale: Float,
    val lineWidth: Float,
    val textSize: Float,
    val width: Int,
    val height: Int,
    val buildings: List<BuildingData>
)