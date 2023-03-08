
package ru.veyukov.arseniy.whiam.navigation.items

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.MenuItem
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import ru.veyukov.arseniy.whiam.MainActivity
import ru.veyukov.arseniy.whiam.MainContextMockkHelper
import ru.veyukov.arseniy.whiam.export.Export
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.wifi.model.WiFiConnection
import ru.veyukov.arseniy.whiam.wifi.model.WiFiData
import ru.veyukov.arseniy.whiam.wifi.model.WiFiDetail
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.veyukov.arseniy.whiam.navigation.items.ExportItem

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ExportItemTest {
    private val export: Export = mockk()
    private val mainActivity: MainActivity = mockk()
    private val menuItem: MenuItem = mockk()
    private val intent: Intent = mockk()
    private val packageManager: PackageManager = mockk()
    private val componentName: ComponentName = mockk()
    private val scanner = MainContextMockkHelper.INSTANCE.scannerService

    private val fixture = ExportItem(export)

    @After
    fun tearDown() {
        confirmVerified(export)
        confirmVerified(mainActivity)
        confirmVerified(menuItem)
        confirmVerified(intent)
        confirmVerified(packageManager)
        confirmVerified(componentName)
        confirmVerified(scanner)
        MainContextMockkHelper.INSTANCE.restore()
    }

    @Test
    fun testActivate() {
        // setup
        val wiFiData: WiFiData = withWiFiData()
        every { scanner.wiFiData() } returns wiFiData
        every { export.export(mainActivity, wiFiData.wiFiDetails) } returns intent
        every { mainActivity.startActivity(intent) } just runs
        every { mainActivity.packageManager } returns packageManager
        every { intent.resolveActivity(packageManager) } returns componentName
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.EXPORT)
        // validate
        verify { scanner.wiFiData() }
        verify { mainActivity.packageManager }
        verify { intent.resolveActivity(packageManager) }
        verify { mainActivity.startActivity(intent) }
        verify { export.export(mainActivity, wiFiData.wiFiDetails) }
    }

    @Test
    fun testRegistered() {
        // execute & validate
        assertFalse(fixture.registered)
    }

    @Test
    fun testVisibility() {
        // execute & validate
        assertEquals(View.GONE, fixture.visibility)
    }

    private fun withWiFiData(): WiFiData {
        return WiFiData(listOf(WiFiDetail.EMPTY), WiFiConnection.EMPTY)
    }

}