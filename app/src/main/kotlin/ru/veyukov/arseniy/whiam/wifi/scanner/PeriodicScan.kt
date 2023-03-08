
package ru.veyukov.arseniy.whiam.wifi.scanner

import android.os.Handler
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.settings.Settings

@OpenClass
internal class PeriodicScan(private val scanner: ScannerService, private val handler: Handler, private val settings: Settings) : Runnable {
    internal var running = false

    fun stop() {
        handler.removeCallbacks(this)
        running = false
    }

    fun start() {
        nextRun(DELAY_INITIAL)
    }

    fun startWithDelay() {
        nextRun(settings.scanSpeed() * DELAY_INTERVAL)
    }

    override fun run() {
        scanner.update()
        startWithDelay()
    }

    private fun nextRun(delay: Long) {
        stop()
        handler.postDelayed(this, delay)
        running = true
    }

    companion object {
        private const val DELAY_INITIAL = 1L
        const val DELAY_INTERVAL = 1000L
    }
}