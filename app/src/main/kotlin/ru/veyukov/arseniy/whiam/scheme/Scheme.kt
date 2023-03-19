package ru.veyukov.arseniy.whiam.scheme

import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.scheme.data.NodeData
import ru.veyukov.arseniy.whiam.scheme.data.SchemeData
import ru.veyukov.arseniy.whiam.scheme.data.WifiNodeData
import ru.veyukov.arseniy.whiam.scheme.data.WifiPointData
import ru.veyukov.arseniy.whiam.scheme.dijkstra.Dijkstra
import ru.veyukov.arseniy.whiam.scheme.dijkstra.Edge
import ru.veyukov.arseniy.whiam.scheme.dijkstra.Graph
import ru.veyukov.arseniy.whiam.util.readFile
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import ru.veyukov.arseniy.whiam.wifi.predicate.makeAccessPointsPredicate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList

class Scheme(val resources: Resources) {
    companion object {
        private const val INVALID_ID = -1
    }

    private val settings = MainContext.INSTANCE.settings
    var schemeData: SchemeData? = null
        get() {
            return field
        }
    var currentBuilding: Int = 0
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var currentFloor: Int = 0
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var targetRoom: Int = INVALID_ID
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var currentPosition: Int = INVALID_ID
        get() {
            return field
        }
    private var pathPoints: ArrayList<Int> = ArrayList<Int>()

    private val maxNodes = 10000
    private lateinit var graph: Graph

    private var wifiNodes = arrayListOf<WifiNodeData>()

    fun getPathPoints(): ArrayList<Int> {
        if (pathMode == 0) return arrayListOf()
        else {
            return pathPoints
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun recalcCurrentPosition() {
        currentPosition = INVALID_ID
        var deltaLevel = 3 //TODO: вынести в параметры
        val wifiPoints = getWiFiPoints()
        var candidateList = arrayListOf<WifiNodeData>()
        wifiPoints.forEach {
            candidateList.addAll(wifiNodes.stream().filter { n ->
                n.mac == it.mac && n.dBm >= it.dBm - deltaLevel && n.dBm <= it.dBm + deltaLevel
            }.toList());
        }
        if (candidateList.size > 0) {
            val frequencies = candidateList.groupingBy { it.id }.eachCount()
            //Toast.makeText(MainContext.INSTANCE.context, frequencies.toString() , Toast.LENGTH_LONG).show()
            if (frequencies.isNotEmpty()) {
                //Toast.makeText(MainContext.INSTANCE.context,Collections.max(frequencies.entries, java.util.Map.Entry.comparingByValue()).key.toString() , Toast.LENGTH_LONG).show()
                currentPosition = Collections.max(frequencies.entries, java.util.Map.Entry.comparingByValue()).key
            }
        }
    }


    fun calcPath() {
        if (currentPosition == INVALID_ID) {
            Toast.makeText(MainContext.INSTANCE.context, "Текущая позиция не определена", Toast.LENGTH_LONG).show()
            return
        }
        if (targetRoom == INVALID_ID) {
            Toast.makeText(MainContext.INSTANCE.context, "Цель не задана", Toast.LENGTH_LONG).show()
            return
        }
        val targetNode =
            schemeData?.buildings?.get(currentBuilding)?.floors?.get(currentFloor)?.rooms?.get(targetRoom)?.node
        if (targetNode == INVALID_ID) {
            Toast.makeText(MainContext.INSTANCE.context, "Цель не связана с контрольной точкой", Toast.LENGTH_LONG)
                .show()
            return
        }
        val d = Dijkstra(graph)
        val dist = targetNode?.let { d.distance(currentPosition, it) }
        pathPoints = d.path
    }

    var nodeList: ArrayList<NodeData> = ArrayList<NodeData>()
        get

    var pathMode = 0
        get

    fun clearNodeList() {
        nodeList.clear()
    }

    fun addNodeToList(nodeData: NodeData) {
        nodeList.add(nodeData)
    }

    fun readSchemeJson(schemeJsonId: Int) {
        schemeData = Gson().fromJson<SchemeData>(readFile(resources, schemeJsonId), SchemeData::class.java)
        currentBuilding = 0
        currentFloor = 0
        targetRoom = INVALID_ID
        fillDijkstraGraph()
        fillWifiNodes()
    }

    fun fillWifiNodes() {
        wifiNodes.clear()
        schemeData!!.buildings.forEach { b ->
            b.floors.forEach { f ->
                f.nodes?.forEach { n ->
                    n.wifi.forEach { w ->
                        if (w.sid != "")
                            wifiNodes.add(WifiNodeData(n.id, w.sid, w.mac, w.dBm))
                    }
                }
            }
        }
    }

    fun fillDijkstraGraph() {
        graph = Graph(maxNodes)
        schemeData!!.buildings.forEach { b ->
            b.floors.forEach { f ->
                f.edges?.forEach { e ->
                    graph.addEdge(Edge(e[0], e[1], 1))
                }
            }
        }
    }

    fun getAllBuildings(): List<String> {
        var allBuildings = ArrayList<String>()
        schemeData!!.buildings.forEach {
            allBuildings.add(it.nameBuilding)
        }
        return allBuildings
    }

    fun getAllFloors(buildingID: Int): List<String> {
        var allFloors = ArrayList<String>()
        schemeData!!.buildings[buildingID].floors.forEach {
            allFloors.add(it.nameFloor)
        }
        return allFloors
    }

    fun getSearchList(): List<String> {
        var result = ArrayList<String>()
        schemeData!!.buildings.forEach { b ->
            b.floors.forEach { f ->
                result.add(b.nameBuilding + " - " + f.nameFloor)
            }
        }
        schemeData!!.buildings.forEach { b ->
            b.floors.forEach { f ->
                f.rooms.forEach { r ->
                    if (r.nameRoom != "") result.add(r.nameRoom + " - " + r.descriptionRoom)
                }
            }
        }
        return result
    }

    fun processSearchResult(index: Int) {
        val floorSize = schemeData!!.buildings[0].floors.size
        if (index < floorSize) {
            currentFloor = index
            MainContext.INSTANCE.mainActivity.title =
                MainContext.INSTANCE.scheme.getAllFloors(currentBuilding)[currentFloor]
        } else {
            var idx = floorSize - 1
            schemeData!!.buildings.forEach { b ->
                b.floors.forEachIndexed { idxFloor, f ->
                    f.rooms.forEachIndexed { idxRoom, r ->
                        if (r.nameRoom != "") {
                            idx++
                            if (idx == index) {
                                currentFloor = idxFloor
                                MainContext.INSTANCE.mainActivity.title =
                                    MainContext.INSTANCE.scheme.getAllFloors(currentBuilding)[currentFloor]
                                targetRoom = idxRoom
                                return@forEach
                            }
                        }
                    }
                }
            }
        }
    }

    fun pathAction() {
        MainContext.INSTANCE.mainActivity.optionMenu.menu?.let { menu ->
            val menuItem = menu.findItem(R.id.action_path)
            menuItem.isVisible = true
            if (pathMode == 0) {
                pathMode = 1
                calcPath()
                menuItem.setIcon(R.drawable.ic_path_on)
            } else {
                pathMode = 0
                menuItem.setIcon(R.drawable.ic_path)
            }
        }
    }

    fun getWiFiPoints(): java.util.ArrayList<WifiPointData> {
        var wifi: java.util.ArrayList<WifiPointData> = arrayListOf()
        val detail: MutableList<WiFiDetail> = mutableListOf()
        val predicate = makeAccessPointsPredicate(settings)
        detail.clear()
        detail.addAll(
            MainContext.INSTANCE.scannerService.wiFiData().wiFiDetails(predicate, settings.sortBy(), settings.groupBy())
        )
        detail.forEach {
            wifi.add(WifiPointData(it.wiFiIdentifier.ssid, it.wiFiIdentifier.bssid, it.wiFiSignal.level))
        }
        wifi = java.util.ArrayList(wifi.distinctBy { it.dBm }.take(5))
        return wifi
    }
}
