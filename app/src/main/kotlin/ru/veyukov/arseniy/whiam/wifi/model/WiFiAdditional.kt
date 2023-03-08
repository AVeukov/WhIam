
package ru.veyukov.arseniy.whiam.wifi.model

import ru.veyukov.arseniy.whiam.util.EMPTY

class WiFiAdditional(val vendorName: String = String.EMPTY,
                     val wiFiConnection: WiFiConnection = WiFiConnection.EMPTY
) {

    companion object {
        val EMPTY = WiFiAdditional()
    }

}