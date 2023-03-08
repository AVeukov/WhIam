
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
        setOnClicks(binding, activity)
        wiFiState(binding)
        return binding.root
    }

    private fun setTexts(binding: AboutContentBinding, activity: FragmentActivity) {
//        binding.aboutCopyright.text = copyright()
        binding.aboutVersionInfo.text = version(activity)
//        binding.aboutPackageName.text = activity.packageName
//        binding.aboutDevice.text = device()
    }

    private fun device(): String =
        Build.MANUFACTURER + " - " + Build.BRAND + " - " + Build.MODEL

    private fun wiFiState(binding: AboutContentBinding) {
        val wiFiManagerWrapper = MainContext.INSTANCE.wiFiManagerWrapper
//        wiFiBand(
//            wiFiManagerWrapper.is5GHzBandSupported(),
//            binding.aboutWifiBand5ghzSuccess,
//            binding.aboutWifiBand5ghzFails
//        )
//        wiFiBand(
//            wiFiManagerWrapper.is6GHzBandSupported(),
//            binding.aboutWifiBand6ghzSuccess,
//            binding.aboutWifiBand6ghzFails
//        )
    }

//    private fun wiFiBand(bandSupported: Boolean, aboutWifiBandSuccess: TextView, aboutWifiBandFails: TextView) {
//        if (bandSupported) {
//            aboutWifiBandSuccess.visibility = View.VISIBLE
//            aboutWifiBandFails.visibility = View.GONE
//        } else {
//            aboutWifiBandSuccess.visibility = View.GONE
//            aboutWifiBandFails.visibility = View.VISIBLE
//        }
//    }

    private fun setOnClicks(binding: AboutContentBinding, activity: FragmentActivity) {
        val gpl = AlertDialogClickListener(activity, R.string.gpl, R.raw.gpl)
        val contributors =
            AlertDialogClickListener(activity, R.string.about_contributor_title, R.raw.contributors, false)
    }
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


    private class WriteReviewClickListener(private val activity: Activity) : View.OnClickListener {
        override fun onClick(view: View) {
            val url = "market://details?id=" + activity.applicationContext.packageName
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            try {
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(view.context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private class AlertDialogClickListener(
        private val activity: Activity,
        private val titleId: Int,
        private val resourceId: Int,
        private val isSmallFont: Boolean = true
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            if (!activity.isFinishing) {
                val text = readFile(activity.resources, resourceId)
                val alertDialog: AlertDialog = AlertDialog.Builder(view.context)
                    .setTitle(titleId)
                    .setMessage(text)
                    .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .create()
                alertDialog.show()
                if (isSmallFont) {
                    alertDialog.findViewById<TextView>(android.R.id.message).textSize = 8f
                }
            }
        }
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