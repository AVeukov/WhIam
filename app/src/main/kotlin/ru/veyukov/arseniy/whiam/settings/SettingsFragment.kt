
package ru.veyukov.arseniy.whiam.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R


open class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        val button = preferenceManager.findPreference<Preference>("wifi_data_clear")
        if (button != null) {
            button.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                MainContext.INSTANCE.scheme.clearNodeList()
                true
            }
        }
        //findPreference<Preference>(getString(R.string.wifi_off_on_exit_key))!!.isVisible = !buildMinVersionQ()
    }

}