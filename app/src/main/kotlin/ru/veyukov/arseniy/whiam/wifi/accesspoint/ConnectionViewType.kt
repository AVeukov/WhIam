
package ru.veyukov.arseniy.whiam.wifi.accesspoint

import androidx.annotation.LayoutRes
import ru.veyukov.arseniy.whiam.R

enum class ConnectionViewType(@LayoutRes val layout: Int) {
    COMPLETE(R.layout.access_point_view_complete),
    COMPACT(R.layout.access_point_view_compact),
    HIDE(R.layout.access_point_view_hide);

    val hide: Boolean
        get() = HIDE == this
}
