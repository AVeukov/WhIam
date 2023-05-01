
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.os.Handler
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.settings.Settings

@OpenClass
internal class PeriodicScan(private val scanner: ScannerService, private val handler: Handler, private val settings: Settings) : Runnable {
    internal var running = false // признак запуска
    fun stop() {
        handler.removeCallbacks(this)
        running = false
    }
    fun start() {
        // запуск с минимальным интервалом
        nextRun(DELAY_INITIAL)
    }
    fun startWithDelay() {
        //запуск с задержкой из настроек
        nextRun(settings.scanSpeed() * DELAY_INTERVAL)
    }
    override fun run() {
        // обновление адаптеров сканера ScannerService
        scanner.update()
        // новый старт с задержкой
        startWithDelay()
    }
    private fun nextRun(delay: Long) {
        stop()
        // задержка
        handler.postDelayed(this, delay)
        running = true
    }
    companion object {
        private const val DELAY_INITIAL = 1L // начальная задержка 1 мс
        const val DELAY_INTERVAL = 1000L // интервал 1 секунда
    }
}