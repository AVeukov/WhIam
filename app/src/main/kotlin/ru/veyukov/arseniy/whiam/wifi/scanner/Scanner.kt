
package ru.veyukov.arseniy.whiam.wifi.scanner

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.permission.PermissionService
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData

@OpenClass
internal class Scanner(
    val wiFiManagerWrapper: WiFiManagerWrapper,
    val settings: Settings,
    val permissionService: PermissionService,
    val transformer: Transformer
) : ScannerService {
    private val updateNotifiers: MutableList<UpdateNotifier> = mutableListOf()

    private var wiFiData: WiFiData = WiFiData.EMPTY
    private var initialScan: Boolean = false

    lateinit var periodicScan: PeriodicScan
    lateinit var scannerCallback: ScannerCallback
    lateinit var scanResultsReceiver: ScanResultsReceiver

    override fun update() {
        wiFiManagerWrapper.enableWiFi()
        if (permissionService.enabled()) {
            scanResultsReceiver.register()
            wiFiManagerWrapper.startScan()
            if (!initialScan) {
                scannerCallback.onSuccess()
                initialScan = true
            }
        }
        wiFiData = transformer.transformToWiFiData()
        updateNotifiers.forEach { it.update(wiFiData) }
    }

    override fun wiFiData(): WiFiData = wiFiData

    override fun register(updateNotifier: UpdateNotifier): Boolean = updateNotifiers.add(updateNotifier)

    override fun unregister(updateNotifier: UpdateNotifier): Boolean = updateNotifiers.remove(updateNotifier)

    override fun pause() {
        periodicScan.stop()
        scanResultsReceiver.unregister()
    }

    override fun running(): Boolean = periodicScan.running

    override fun resume(): Unit = periodicScan.start()

    override fun resumeWithDelay(): Unit = periodicScan.startWithDelay()

    override fun stop() {
        periodicScan.stop()
        updateNotifiers.clear()
//        if (settings.wiFiOffOnExit()) {
//            wiFiManagerWrapper.disableWiFi()
//        }
        scanResultsReceiver.unregister()
    }

    override fun toggle(): Unit =
        if (periodicScan.running) {
            periodicScan.stop()
        } else {
            periodicScan.start()
        }

    fun registered(): Int = updateNotifiers.size

}