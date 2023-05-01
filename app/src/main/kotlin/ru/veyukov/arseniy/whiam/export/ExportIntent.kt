

package ru.veyukov.arseniy.whiam.export

import android.content.Intent
import ru.veyukov.arseniy.whiam.annotation.OpenClass

@OpenClass
class ExportIntent {
    // формируем Intent
    internal fun intent(title: String, data: String): Intent {
        val intentSend: Intent = intentSend()
        intentSend.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TITLE, title)
        intentSend.putExtra(Intent.EXTRA_SUBJECT, title)
        intentSend.putExtra(Intent.EXTRA_TEXT, data)
        return intentChooser(intentSend, title)
    }
    // Intent ACTION_SEND - отправить куда угодно, в Телеграм, в буфер, в файл и т.п.
    internal fun intentSend(): Intent = Intent(Intent.ACTION_SEND)

    internal fun intentChooser(intent: Intent, title: String): Intent = Intent.createChooser(intent, title)
}