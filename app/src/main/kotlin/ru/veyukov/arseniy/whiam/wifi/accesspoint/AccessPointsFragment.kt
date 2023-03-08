
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import ru.veyukov.arseniy.whiam.util.buildVersionP
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.databinding.AccessPointsContentBinding

class AccessPointsFragment : Fragment(), OnRefreshListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var accessPointsAdapter: AccessPointsAdapter
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = AccessPointsContentBinding.inflate(inflater, container, false)
        swipeRefreshLayout = binding.accessPointsRefresh
        swipeRefreshLayout.setOnRefreshListener(this)
        if (buildVersionP()) {
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.isEnabled = false
        }
        accessPointsAdapter = AccessPointsAdapter()
        binding.accessPointsView.setAdapter(accessPointsAdapter)
        accessPointsAdapter.expandableListView = binding.accessPointsView
        return binding.root
    }

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        MainContext.INSTANCE.scannerService.update()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        MainContext.INSTANCE.scannerService.register(accessPointsAdapter)
        onRefresh()
    }

    override fun onPause() {
        MainContext.INSTANCE.scannerService.unregister(accessPointsAdapter)
        super.onPause()
    }

}