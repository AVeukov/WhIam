
package ru.veyukov.arseniy.whiam.app

import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.manager.WiFiManagerWrapper
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService
import io.mockk.mockk
import ru.veyukov.arseniy.whiam.Configuration
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContext

enum class MainContextMockkHelper {
    INSTANCE;

    private val saved: MutableMap<Class<*>, Any> = mutableMapOf()
    private val mainContext: MainContext = MainContext.INSTANCE

    val settings: Settings
        get() {
            try {
                saved[Settings::class.java] = mainContext.settings
            } catch (e: UninitializedPropertyAccessException) {
                /* do nothing */
            }
            mainContext.settings = mockk()
            return mainContext.settings
        }


    val scannerService: ScannerService
        get() {
            try {
                saved[ScannerService::class.java] = mainContext.scannerService
            } catch (e: UninitializedPropertyAccessException) {
                /* do nothing */
            }
            mainContext.scannerService = mockk()
            return mainContext.scannerService
        }

    val mainActivity: MainActivity
        get() {
            try {
                saved[MainActivity::class.java] = mainContext.mainActivity
            } catch (e: UninitializedPropertyAccessException) {
                /* do nothing */
            }
            mainContext.mainActivity = mockk()
            return mainContext.mainActivity
        }

    val configuration: Configuration
        get() {
            try {
                saved[Configuration::class.java] = mainContext.configuration
            } catch (e: UninitializedPropertyAccessException) {
                /* do nothing */
            }
            mainContext.configuration = mockk()
            return mainContext.configuration
        }


    val wiFiManagerWrapper: WiFiManagerWrapper
        get() {
            try {
                saved[WiFiManagerWrapper::class.java] = mainContext.wiFiManagerWrapper
            } catch (e: UninitializedPropertyAccessException) {
                /* do nothing */
            }
            mainContext.wiFiManagerWrapper = mockk()
            return mainContext.wiFiManagerWrapper
        }

    fun restore() {
        saved.entries.forEach {
            when (it.key) {
                Settings::class.java -> mainContext.settings = it.value as Settings
                ScannerService::class.java -> mainContext.scannerService = it.value as ScannerService
                MainActivity::class.java -> mainContext.mainActivity = it.value as MainActivity
                Configuration::class.java -> mainContext.configuration = it.value as Configuration
                WiFiManagerWrapper::class.java -> mainContext.wiFiManagerWrapper = it.value as WiFiManagerWrapper
            }
        }
        saved.clear()
    }

}
