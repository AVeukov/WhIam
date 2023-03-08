
package ru.veyukov.arseniy.whiam.wifi.scanner

import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper

@OpenClass
internal class ScannerCallback(private val wiFiManagerWrapper: WiFiManagerWrapper, private val cache: Cache) :
    Callback {

    override fun onSuccess() {
        cache.add(wiFiManagerWrapper.scanResults())
        cache.wifiInfo = wiFiManagerWrapper.wiFiInfo()
    }

}