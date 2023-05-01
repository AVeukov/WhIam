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
        //константа - несуществующий ID
        private const val INVALID_ID = -1
    }
    private val settings = MainContext.INSTANCE.settings // настройки - забираем из основного контекста приложения
    var schemeData: SchemeData? = null // данные схемы - дата класс
        get() {
            return field
        }
    var currentBuilding: Int = 0     //текущее здание -  по умолчанию 0, т.е. первое здание из схемы
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var currentFloor: Int = 0     //текущий этаж -  по умолчанию 0, т.е. первый этаж
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var targetRoom: Int = INVALID_ID     //целевая комната -  по умолчанию INVALID_ID
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var currentPosition: Int = INVALID_ID //текущая позиция
        get() {
            return field
        }
    private var pathPoints: ArrayList<Int> = ArrayList<Int>()  // массив точек пути от текущей позиции к целевой комнате

    private val maxNodes = 10000 // максимальное количество узлов в графе для алгоритма Дейкстры
    private lateinit var graph: Graph //граф алгоритма Дейкстры (отложенная инициализация lateinit)

    private var wifiNodes = arrayListOf<WifiNodeData>() //wifi данные ключевых точек из файла schema.json

    fun getPathPoints(): ArrayList<Int> { // метод возвращающий массив точек пути
        if (pathMode == 0) return arrayListOf() // пустой массив, если режим
        else {
            return pathPoints // массив точек пути - определяется в методе calcPath()
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun recalcCurrentPosition() { //определение текущей позиции, используется в SchemeView и ScannerSwitch (для определения позиции  при переключении)
        currentPosition = INVALID_ID // сброс текущей позиции
        var deltaLevel = 3  //дельта уровня сигнала +/- - надо добавить в настройки
        val wifiPoints = getWiFiPoints() // Точки доступа с уровнями сигнала в текущей точке
        var candidateList = arrayListOf<WifiNodeData>() // массив контрольных точек - кандидатов, у которых MAC адрес и уровень сигнала совпадает (+/-) с текущей
        wifiPoints.forEach { w -> //цикл по точкам доступа
            candidateList.addAll(wifiNodes.stream().filter { n ->  // преобразуем массив в стрим для более простой фильтрации
                n.mac == w.mac && n.dBm >= w.dBm - deltaLevel && n.dBm <= w.dBm + deltaLevel
            }.toList()); // добавляем в кандидаты, только те точки у которых МАК адрес и уровень +/- дельта совпадают
        }
        if (candidateList.size > 0) { // если массив кандидатов не пустой
            val frequencies = candidateList.groupingBy { it.id }.eachCount() //считаем количество совпадений, группируя по ID контрольной точки из wifiNodes
            //Toast.makeText(MainContext.INSTANCE.context, frequencies.toString() , Toast.LENGTH_LONG).show()
            if (frequencies.isNotEmpty()) {
                //Toast.makeText(MainContext.INSTANCE.context,Collections.max(frequencies.entries, java.util.Map.Entry.comparingByValue()).key.toString() , Toast.LENGTH_LONG).show()
                currentPosition = Collections.max(frequencies.entries, java.util.Map.Entry.comparingByValue()).key // выбираем ID контрольной точки с максимальный количеством совпадений МАС - уровень сигнала
            }
        }
    }
    fun calcPath() { //расчет пути по алгоритму Дейкстры, используется в текущем классе в fun pathAction()
        if (currentPosition == INVALID_ID) { // Текущая позиция не определена - расчет невозможен
            Toast.makeText(MainContext.INSTANCE.context, "Текущая позиция не определена", Toast.LENGTH_LONG).show()
            return
        }
        if (targetRoom == INVALID_ID) { // Цель не задана - расчет невозможен
            Toast.makeText(MainContext.INSTANCE.context, "Цель не задана", Toast.LENGTH_LONG).show()
            return
        }
        val targetNode =
            schemeData?.buildings?.get(currentBuilding)?.floors?.get(currentFloor)?.rooms?.get(targetRoom)?.node
        if (targetNode == INVALID_ID) { // Цель не связана с контрольной точкой - расчет невозможен
            Toast.makeText(MainContext.INSTANCE.context, "Цель не связана с контрольной точкой", Toast.LENGTH_LONG)
                .show()
            return
        }
        val d = Dijkstra(graph) // создание объекта для расчета пути по алгоритму Дейкстры
        val dist = targetNode?.let { d.distance(currentPosition, it) } // расчет кратчайшей дистанции (путь заполняется внутри)
        pathPoints = d.path // возврат пути
    }

    var nodeList: ArrayList<NodeData> = ArrayList<NodeData>() // массив контрольных точек, при фиксации их на схеме (перед экспортом)
        get

    var pathMode = 0 // режим работы: 1 - расчет и отображение пути, 0 - путь не отображается
        get

    fun clearNodeList() { // очистка массива контрольных точек
        nodeList.clear()
    }

    fun addNodeToList(nodeData: NodeData) { // добавить точку в массив контрольных точек
        nodeList.add(nodeData)
    }

    fun readSchemeJson(schemeJsonId: Int) { // чтение JSON файла и присвоение значений дата классу SchemeData
        // используется в классе MainActivity метод OnCreate - создание Activity
        schemeData = Gson().fromJson<SchemeData>(readFile(resources, schemeJsonId), SchemeData::class.java) // используется библиотека Gson
        // начальные значения параметров
        currentBuilding = 0
        currentFloor = 0
        targetRoom = INVALID_ID
        fillDijkstraGraph() // заполнение графа для алгоритма Дейкстры
        fillWifiNodes() // подготовка массива контрольных точек
    }

    fun fillWifiNodes() { // подготовка массива контрольных точек - данные из дата класса
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

    fun fillDijkstraGraph() { // заполнение графа для алгоритма Дейкстры
        graph = Graph(maxNodes) // создание объекта графа
        schemeData!!.buildings.forEach { b -> // в циклах по ребрам edges добавление их в граф
            b.floors.forEach { f ->
                f.edges?.forEach { e ->
                    graph.addEdge(Edge(e[0], e[1], 1))
                }
            }
        }
    }

    fun getAllBuildings(): List<String> { // получение всех зданий
        var allBuildings = ArrayList<String>()
        schemeData!!.buildings.forEach {
            allBuildings.add(it.nameBuilding)
        }
        return allBuildings
    }

    fun getAllFloors(buildingID: Int): List<String> { // получение всех этажей по зданию buildingID
        var allFloors = ArrayList<String>()
        schemeData!!.buildings[buildingID].floors.forEach {
            allFloors.add(it.nameFloor)
        }
        return allFloors
    }

    fun getSearchList(): List<String> { // подготовка данных для списка поиска, используется в классе OptionMenu fun createSearchView
        var result = ArrayList<String>()
        schemeData!!.buildings.forEach { b -> // сначала добавляем все этажи
            b.floors.forEach { f ->
                result.add(b.nameBuilding + " - " + f.nameFloor)
            }
        }
        schemeData!!.buildings.forEach { b -> // затем добавляем все комнаты
            b.floors.forEach { f ->
                f.rooms.forEach { r ->
                    if (r.nameRoom != "") result.add(r.nameRoom + " - " + r.descriptionRoom) // добавляем только комнаты у которых есть имя
                }
            }
        }
        return result
    }

    fun processSearchResult(index: Int) { // обработка результатов поиска Index в массиве getSearchList() используется в классе OptionMenu
        val floorSize = schemeData!!.buildings[0].floors.size
        if (index < floorSize) { // вначале идут этажи
            currentFloor = index // текущий этаж
            MainContext.INSTANCE.mainActivity.title = // меняем заголовок на окне
                MainContext.INSTANCE.scheme.getAllFloors(currentBuilding)[currentFloor]
        } else {
            var idx = floorSize - 1 // индекс начинается с floorSize минус 1
            schemeData!!.buildings.forEach { b ->
                b.floors.forEachIndexed { idxFloor, f ->
                    f.rooms.forEachIndexed { idxRoom, r ->
                        if (r.nameRoom != "") { // обрабатываем только комнаты у которых есть имя
                            idx++ // последовательно увеличиваем индекс
                            if (idx == index) {  // индексы совпадают
                                currentFloor = idxFloor // текущий этаж
                                MainContext.INSTANCE.mainActivity.title = // меняем заголовок
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
    fun pathAction() { // действия при нажатии кнопки Путь используется в OptionAction
        MainContext.INSTANCE.mainActivity.optionMenu.menu?.let { menu ->
            val menuItem = menu.findItem(R.id.action_path) // ищем пункт меню, по ID в ресурсах
            menuItem.isVisible = true
            if (pathMode == 0) { // переключаем
                pathMode = 1 // режим поиска пути
                calcPath() // расчет пути по алгоритму Дейкстра
                menuItem.setIcon(R.drawable.ic_path_on)// меняем картинку - но она почему-то не меняется
            } else {
                pathMode = 0 // режим без пути
                menuItem.setIcon(R.drawable.ic_path)
            }
        }
    }

    fun getWiFiPoints(): java.util.ArrayList<WifiPointData> { // получение текущей информации по Wi-Fi точкам доступаю Используется в текущем классе и SchemeView
        var wifi: java.util.ArrayList<WifiPointData> = arrayListOf() //массив текущих точек
        val detail: MutableList<WiFiDetail> = mutableListOf() // детальная информация по Wi-Fi
        val predicate = makeAccessPointsPredicate(settings) // предикат для фильтрации по настройкам (уровень сигнала)
        detail.clear()
        detail.addAll( // получаем информацию из scannerService, с фильтрацией и сортировкой по уровню
            MainContext.INSTANCE.scannerService.wiFiData().wiFiDetails(predicate, settings.sortBy(), settings.groupBy())
        )
        detail.forEach { // заполнение результирующего массива
            wifi.add(WifiPointData(it.wiFiIdentifier.ssid, it.wiFiIdentifier.bssid, it.wiFiSignal.level))
        }
        wifi = java.util.ArrayList(wifi.distinctBy { it.dBm }.take(5)) // удаление точек с одинаковым уровнем (т.к. на одном роутере может быть несколько точек) и оставляем 5 точек с самым высоким уровнем сигнала
        return wifi
    }
}