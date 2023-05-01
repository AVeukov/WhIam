

package ru.veyukov.arseniy.whiam.export

import android.content.Intent
import android.content.res.Resources
import com.google.gson.Gson
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.scheme.data.NodeData
import java.text.SimpleDateFormat
import java.util.*

class Export(private val exportIntent: ExportIntent = ExportIntent()) {

    fun export(mainActivity: MainActivity, nodes: List<NodeData>): Intent =
        export(mainActivity, nodes, Date())

    fun export(mainActivity: MainActivity, nodes: List<NodeData>, date: Date): Intent {
        val timestamp: String = timestamp(date)
        val title: String = title(mainActivity, timestamp)
        val data: String = data(nodes, timestamp)
        return exportIntent.intent(title, data)
    }

    internal fun data(nodes: List<NodeData>, timestamp: String): String =
        Gson().toJson(nodes)

    internal fun title(mainActivity: MainActivity, timestamp: String): String {
        val resources: Resources = mainActivity.resources
        val title: String = resources.getString(R.string.action_access_points)
        return "$title-$timestamp"
    }

    internal fun timestamp(date: Date): String = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(date)


    companion object {
        private const val TIME_STAMP_FORMAT = "yyyy/MM/dd-HH:mm:ss"
    }

}