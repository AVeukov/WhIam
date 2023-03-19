package ru.veyukov.arseniy.whiam.scheme.data

enum class RoomTypes {
    CLASS, HALL
}
data class RoomData (
    val nameRoom: String,
    val descriptionRoom: String,
    val typeRoom: RoomTypes,
    val walls: List<Int>,
    val doors: List<Int>,
    val node: Int = -1
)