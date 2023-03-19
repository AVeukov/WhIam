package ru.veyukov.arseniy.whiam.scheme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.databinding.FragmentSchemeBinding

class SchemeFragment : Fragment(), OnRefreshListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var schemeAdapter: SchemeAdapter
        private set
    var schemeView: SchemeView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSchemeBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_scheme, container, false)
        val relativeLayout = view.findViewById(R.id.idRLView) as RelativeLayout
        schemeView = SchemeView(requireActivity())
        relativeLayout.addView(schemeView)
        swipeRefreshLayout = binding.schemeRefresh
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.setSize(0)
        schemeAdapter = SchemeAdapter(schemeView!!)
         return view
    }
    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        MainContext.INSTANCE.scannerService.update()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        MainContext.INSTANCE.scannerService.register(schemeAdapter)
        onRefresh()
    }

    override fun onPause() {
        MainContext.INSTANCE.scannerService.unregister(schemeAdapter)
        super.onPause()
    }
}