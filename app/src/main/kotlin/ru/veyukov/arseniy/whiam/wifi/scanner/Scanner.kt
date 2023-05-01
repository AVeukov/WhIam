
package ru.veyukov.arseniy.whiam.wifi.scanner

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.permission.PermissionService
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData

@OpenClass
internal class Scanner(
    val wiFiManagerWrapper: WiFiManagerWrapper, // обертка для WiFiManager
    val settings: Settings, // настройки
    val permissionService: PermissionService, // разрешения
    val transformer: Transformer //класс преобразований
) : ScannerService {
    private val updateNotifiers: MutableList<UpdateNotifier> = mutableListOf() //уведомители обновления

    private var wiFiData: WiFiData = WiFiData.EMPTY // данные WiFi
    private var initialScan: Boolean = false // начальный экран

    lateinit var periodicScan: PeriodicScan // периодическое сканирование
    lateinit var scannerCallback: ScannerCallback //коллбэк - возвратная функция
    lateinit var scanResultsReceiver: ScanResultsReceiver // получение данных сканирования

    override fun update() { // обновление
        wiFiManagerWrapper.enableWiFi() // включение WiFi
        if (permissionService.enabled()) { // если разрешение есть
            scanResultsReceiver.register() // регистрируем приемник данных
            wiFiManagerWrapper.startScan() // старт сканирования
            if (!initialScan) {
                scannerCallback.onSuccess() // сканирование успешно
                initialScan = true
            }
        }
        wiFiData = transformer.transformToWiFiData() // преобразование данных
        updateNotifiers.forEach { it.update(wiFiData) } // обновление
    }

    override fun wiFiData(): WiFiData = wiFiData // данные WiFI

    override fun register(updateNotifier: UpdateNotifier): Boolean = updateNotifiers.add(updateNotifier) //регистрация уведомителя

    override fun unregister(updateNotifier: UpdateNotifier): Boolean = updateNotifiers.remove(updateNotifier) // разрегистрация уведомителя

    override fun pause() { //пауза
        periodicScan.stop()
        scanResultsReceiver.unregister()
    }

    override fun running(): Boolean = periodicScan.running // признак работы

    override fun resume(): Unit = periodicScan.start() // старт сканирования

    override fun resumeWithDelay(): Unit = periodicScan.startWithDelay() // задержка

    override fun stop() {
        periodicScan.stop()
        updateNotifiers.clear()
//        if (settings.wiFiOffOnExit()) {
//            wiFiManagerWrapper.disableWiFi()
//        }
        scanResultsReceiver.unregister()
    }

    override fun toggle(): Unit = // переключение
        if (periodicScan.running) {
            periodicScan.stop()
        } else {
            periodicScan.start()
        }

    fun registered(): Int = updateNotifiers.size

}