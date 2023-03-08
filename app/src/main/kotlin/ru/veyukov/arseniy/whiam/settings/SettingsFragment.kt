
package ru.veyukov.arseniy.whiam.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.veyukov.arseniy.whiam.util.buildMinVersionQ
import ru.veyukov.arseniy.whiam.R

open class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        //findPreference<Preference>(getString(R.string.wifi_off_on_exit_key))!!.isVisible = !buildMinVersionQ()
    }

}