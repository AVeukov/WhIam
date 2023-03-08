package ru.veyukov.arseniy.whiam.scheme

import android.content.res.Resources
import com.google.gson.Gson
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.util.readFile

class Scheme(val resources: Resources) {
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

    fun readSchemeJson(schemeJsonId: Int) {
        schemeData = Gson().fromJson<SchemeData>(readFile(resources, schemeJsonId), SchemeData::class.java)
        currentBuilding = 0
        currentFloor = 0

    }
    fun  getAllBuildings( ):List<String>{
        var allBuildings =  ArrayList<String>()
        schemeData!!.buildings.forEach {
            allBuildings.add(it.nameBuilding)
        }
        return allBuildings
    }
    fun  getAllFloors( buildingID: Int ):List<String>{
        var allFloors =  ArrayList<String>()
        schemeData!!.buildings[buildingID].floors.forEach {
            allFloors.add(it.nameFloor)
        }
        return allFloors
    }
    fun getSearchList():List<String>{
        var result = ArrayList<String>()
        schemeData!!.buildings.forEach{ b ->
            b.floors.forEach {f->
                result.add(b.nameBuilding+" - "+ f.nameFloor)
            }
        }
        schemeData!!.buildings.forEach{ b ->
            b.floors.forEach {f->
                f.rooms.forEach { r->
                    if(r.nameRoom != "") result.add(r.nameRoom+" - "+ r.descriptionRoom)
                }
            }
        }
return result
    }
}