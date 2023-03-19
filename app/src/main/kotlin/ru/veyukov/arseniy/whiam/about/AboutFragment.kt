
package ru.veyukov.arseniy.whiam.about

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.util.packageInfo
import ru.veyukov.arseniy.whiam.util.readFile
import ru.veyukov.arseniy.whiam.MainContext
import ru.veyukov.arseniy.whiam.R
import ru.veyukov.arseniy.whiam.databinding.AboutContentBinding
import java.text.SimpleDateFormat
import java.util.*

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: AboutContentBinding = AboutContentBinding.inflate(inflater, container, false)
        val activity: FragmentActivity = requireActivity()
        setTexts(binding, activity)
        return binding.root
    }

    private fun setTexts(binding: AboutContentBinding, activity: FragmentActivity) {
        binding.aboutVersionInfo.text = version(activity)

    }

    private fun device(): String =
        Build.MANUFACTURER + " - " + Build.BRAND + " - " + Build.MODEL


    private fun version(activity: FragmentActivity): String {
        val configuration = MainContext.INSTANCE.configuration
        return applicationVersion(activity) +
                ifElse(configuration.sizeAvailable, "S") +
                ifElse(configuration.largeScreen, "L") +
                " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")"
    }

    private fun applicationVersion(activity: FragmentActivity): String =
        try {
            val packageInfo: PackageInfo = activity.packageInfo()
            packageInfo.versionName + " - " + PackageInfoCompat.getLongVersionCode(packageInfo)
        } catch (e: NameNotFoundException) {
            String.EMPTY
        }

    private fun ifElse(condition: Boolean, value: String) =
        if (condition) {
            value
        } else {
            String.EMPTY
        }


    companion object {
        private const val YEAR_FORMAT = "yyyy"
    }
}