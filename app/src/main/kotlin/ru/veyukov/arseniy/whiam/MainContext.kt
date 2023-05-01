
package ru.veyukov.arseniy.whiam

import android.content.Context
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import ru.veyukov.arseniy.whiam.scheme.Scheme
import ru.veyukov.arseniy.whiam.settings.Repository
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService
import ru.veyukov.arseniy.whiam.wifi.scanner.makeScannerService

enum class MainContext {
    INSTANCE;

    lateinit var settings: Settings // настройки
    lateinit var mainActivity: MainActivity // основной Activity
    lateinit var wiFiManagerWrapper: WiFiManagerWrapper // оболочка для WiFiManager
    lateinit var scannerService: ScannerService // сервис сканирования
    lateinit var configuration: Configuration // конфигурация
    lateinit var scheme: Scheme // Схема

    val context: Context // основной контекст Activity
        get() = mainActivity.applicationContext

    val resources: Resources // ресурсы
        get() = context.resources

    val layoutInflater: LayoutInflater //автоматически присоединяет элементы интерфейса
        get() = mainActivity.layoutInflater

    private val wiFiManager: WifiManager // менеджер работы с Wi-FI - системный сервис
        get() = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun initialize(activity: MainActivity, largeScreen: Boolean) { //инициализация
        // создаем и присваиваем объекты
        mainActivity = activity
        configuration = Configuration(largeScreen)
        settings = Settings(Repository(context))
        wiFiManagerWrapper = WiFiManagerWrapper(wiFiManager)
        //сервис периодического сканирования
        scannerService = makeScannerService(mainActivity, wiFiManagerWrapper, Handler(Looper.getMainLooper()), settings)
        scheme=Scheme(activity.resources)
    }

}