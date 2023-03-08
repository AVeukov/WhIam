package ru.veyukov.arseniy.whiam.scheme

data class FloorData(
    val nameFloor: String,
    val rooms: List<RoomData>,
    val halls: List<HallData>,
    val partitions: List<PartitionData>,
    val stairs: List<StairsData>,
    val nodes: List<NodeData>,
    val edges: List<List<Int>>
)
