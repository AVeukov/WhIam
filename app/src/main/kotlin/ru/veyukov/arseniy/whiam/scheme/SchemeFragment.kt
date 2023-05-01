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
//        try {
//            val f: Field = swipeRefreshLayout.javaClass.getDeclaredField("mCircleView")
//            f.setAccessible(true)
//            val img: ImageView = f.get(swipeRefreshLayout) as ImageView
//            img.setAlpha(0.0f)
//        } catch (e: NoSuchFieldException) {
//            e.printStackTrace()
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        }
        return view
    }
    override fun onRefresh() { // метод интерфейса OnRefreshListener
        // обновление
        swipeRefreshLayout.isRefreshing = true
        // обновляем адаптеры зарегистрированные в периодическом сервисе сканирования
        MainContext.INSTANCE.scannerService.update()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onResume() { // метод интерфейса OnRefreshListener
        //возобновление работы
        // срабатывает при переходе на фрагмент
        super.onResume()
        // регистрируем адаптер в периодическом сервисе сканирования
        MainContext.INSTANCE.scannerService.register(schemeAdapter)
        onRefresh()
    }

    override fun onPause() { // метод интерфейса OnRefreshListener
        // пауза, срабатывает после ухода на другой экран
        // разрегистрируем адаптер в периодическом сервисе сканирования
        // чтобы он не срабатывал
        MainContext.INSTANCE.scannerService.unregister(schemeAdapter)
        super.onPause()
    }
}