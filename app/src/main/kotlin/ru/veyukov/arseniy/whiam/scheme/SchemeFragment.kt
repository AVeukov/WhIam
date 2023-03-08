package ru.veyukov.arseniy.whiam.scheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R

class SchemeFragment : Fragment() {
    var schemeView : SchemeView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_scheme, container, false)
        val relativeLayout = view.findViewById(R.id.idRLView) as RelativeLayout
        //createBuildingSpinner(view)
        //createFloorSpinner(view)
        schemeView = SchemeView(requireActivity())
        relativeLayout.addView(schemeView)

        return view
    }
//    fun createBuildingSpinner(view:View){
//        val spinner: Spinner = view.findViewById(R.id.building_spinner)
//        val adapter: ArrayAdapter<*>
//        adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, MainContext.INSTANCE.scheme.getAllBuildings())
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.adapter = adapter
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//                MainContext.INSTANCE.scheme.currentBuilding = position
//                schemeView?.invalidate()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//            }
//        }
//    }
//    fun createFloorSpinner(view:View){
//        val spinner: Spinner = view.findViewById(R.id.floor_spinner)
//        val adapter: ArrayAdapter<*>
//        adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, MainContext.INSTANCE.scheme.getAllFloors(MainContext.INSTANCE.scheme.currentBuilding))
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.adapter = adapter
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View, position: Int, id: Long
//            ) {
//                MainContext.INSTANCE.scheme.currentFloor = position
//                schemeView?.invalidate()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//            }
//        }
//    }
//        companion object {
//
//            fun newInstance(color: Int): SchemeFragment {
//
//                val args = Bundle()
//                args.putInt("color", color)
//                val fragment = SchemeFragment()
//                fragment.arguments = args
//                return fragment
//            }
//        }
}