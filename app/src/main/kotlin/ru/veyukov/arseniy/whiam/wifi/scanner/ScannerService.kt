
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.os.Handler
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.permission.PermissionService
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData

interface UpdateNotifier {
    fun update(wiFiData: WiFiData)
}

interface ScannerService {
    fun update()
    fun wiFiData(): WiFiData
    fun register(updateNotifier: UpdateNotifier): Boolean
    fun unregister(updateNotifier: UpdateNotifier): Boolean
    fun pause()
    fun running(): Boolean
    fun resume()
    fun resumeWithDelay()
    fun stop()
    fun toggle()
}

fun makeScannerService(
    mainActivity: MainActivity,
    wiFiManagerWrapper: WiFiManagerWrapper,
    handler: Handler,
    settings: Settings
): ScannerService {
    val cache = Cache()
    val transformer = Transformer(cache)
    val permissionService = PermissionService(mainActivity)
    val scanner = Scanner(wiFiManagerWrapper, settings, permissionService, transformer)
    scanner.periodicScan = PeriodicScan(scanner, handler, settings)
    scanner.scannerCallback = ScannerCallback(wiFiManagerWrapper, cache)
    scanner.scanResultsReceiver = ScanResultsReceiver(mainActivity, scanner.scannerCallback)
    return scanner
}
