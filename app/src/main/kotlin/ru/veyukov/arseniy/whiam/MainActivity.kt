
package ru.veyukov.arseniy.whiam

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import ru.veyukov.arseniy.whiam.annotation.OpenClass
import ru.veyukov.arseniy.whiam.util.EMPTY
import ru.veyukov.arseniy.whiam.util.createContext
import ru.veyukov.arseniy.whiam.util.findOne
import ru.veyukov.arseniy.whiam.navigation.NavigationMenu
import ru.veyukov.arseniy.whiam.navigation.NavigationMenuControl
import ru.veyukov.arseniy.whiam.navigation.NavigationMenuController
import ru.veyukov.arseniy.whiam.navigation.options.OptionMenu
import ru.veyukov.arseniy.whiam.permission.PermissionService
import ru.veyukov.arseniy.whiam.settings.Repository
import ru.veyukov.arseniy.whiam.settings.Settings
import ru.veyukov.arseniy.whiam.wifi.scanner.ScannerService

@OpenClass
class MainActivity : AppCompatActivity(), NavigationMenuControl, OnSharedPreferenceChangeListener {
    internal lateinit var drawerNavigation: DrawerNavigation // боковая панель
    internal lateinit var mainReload: MainReload // клас отвечающий за обновление при смене темы или языка приложения
    internal lateinit var navigationMenuController: NavigationMenuController // меню навигации
    internal lateinit var optionMenu: OptionMenu // верхнее меню (старт/пауза, путь, поиск)
    internal lateinit var permissionService: PermissionService // сервис разрешений
    //internal lateinit var connectionView: ConnectionView

    private var currentCountryCode: String = String.EMPTY // КОД СТРАНЫ

    override fun attachBaseContext(newBase: Context) =
        super.attachBaseContext(newBase.createContext(Settings(Repository(newBase)).languageLocale()))

    override fun onCreate(savedInstanceState: Bundle?) { // создание

        val mainContext = MainContext.INSTANCE // основной контекст
        mainContext.initialize(this, largeScreen) //инициализация

        // настройки
        val settings = mainContext.settings
        settings.initializeDefaultValues() // начальные значения
        setTheme(settings.themeStyle().themeNoActionBar) // тема
        setWiFiChannelPairs(mainContext)

        val scheme=mainContext.scheme // схема
        scheme.readSchemeJson(R.raw.schema) // чтение схемы из JSON файла

        mainReload = MainReload(settings) // клас отвечающий за обновление при смене темы или языка приложения

        super.onCreate(savedInstanceState)
        installSplashScreen() // начальный экран, показывается пока приложение запускается
        setContentView(R.layout.main_activity) // основное View

        settings.registerOnSharedPreferenceChangeListener(this) // обработчиком при изменении настроек будет этот класс
        optionMenu = OptionMenu() // меню - верхнее (старт/пауза, путь, поиск)

        keepScreenOn() // экран всегда включен

        val toolbar = setupToolbar() // боковая панель
        drawerNavigation = DrawerNavigation(this, toolbar)
        drawerNavigation.create()

        // навигационное меню
        navigationMenuController = NavigationMenuController(this)
        navigationMenuController.currentNavigationMenu(settings.selectedMenu())
        onNavigationItemSelected(currentMenuItem())

        //connectionView = ConnectionView(this)
        // сервис разрешений
        permissionService = PermissionService(this)

        // обработчик нажатия кнопки Назад
        onBackPressedDispatcher.addCallback(this, MainActivityBackPressed(this))
    }

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerNavigation.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerNavigation.onConfigurationChanged(newConfig)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (!permissionService.granted(requestCode, grantResults)) {
            finish()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setWiFiChannelPairs(mainContext: MainContext) {
        val settings = mainContext.settings
        val countryCode = settings.countryCode()
        if (countryCode != currentCountryCode) {
            mainContext.configuration.wiFiChannelPair(countryCode)
            currentCountryCode = countryCode
        }
    }

    private val largeScreen: Boolean
        get() {
            val configuration = resources.configuration
            val screenLayoutSize = configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
            return screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                    screenLayoutSize == Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val mainContext = MainContext.INSTANCE
        if (mainReload.shouldReload(mainContext.settings)) {
            MainContext.INSTANCE.scannerService.stop()
            recreate()
        } else {
            keepScreenOn()
            setWiFiChannelPairs(mainContext)
            update()
        }
    }

    fun update() {
        MainContext.INSTANCE.scannerService.update()
        updateActionBar()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        closeDrawer()
        val currentNavigationMenu = findOne(NavigationMenu.values(), menuItem.itemId, NavigationMenu.ACCESS_POINTS)
        currentNavigationMenu.activateNavigationMenu(this, menuItem)
        return true
    }

    fun closeDrawer(): Boolean {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return true
        }
        return false
    }

    public override fun onPause() {
        val scannerService: ScannerService = MainContext.INSTANCE.scannerService
        scannerService.pause()
        //scannerService.unregister(connectionView)
        updateActionBar()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        val scannerService: ScannerService = MainContext.INSTANCE.scannerService
        if (permissionService.permissionGranted()) {
            if (!permissionService.systemEnabled()) {
                startLocationSettings()
            }
            scannerService.resume()
        } else {
            scannerService.pause()
        }
        updateActionBar()
        //scannerService.register(connectionView)
    }

    public override fun onStop() {
        MainContext.INSTANCE.scannerService.stop()
        super.onStop()
    }

    public override fun onStart() {
        super.onStart()
        if (permissionService.permissionGranted()) {
            MainContext.INSTANCE.scannerService.resume()
        } else {
            permissionService.check()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        optionMenu.create(this, menu)
        updateActionBar()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        optionMenu.select(item)
        updateActionBar()
        return true
    }

    fun updateActionBar() = currentNavigationMenu().activateOptions(this)

    override fun currentMenuItem(): MenuItem = navigationMenuController.currentMenuItem()

    override fun currentNavigationMenu(): NavigationMenu = navigationMenuController.currentNavigationMenu()

    override fun currentNavigationMenu(navigationMenu: NavigationMenu) {
        navigationMenuController.currentNavigationMenu(navigationMenu)
        MainContext.INSTANCE.settings.saveSelectedMenu(navigationMenu)
    }

    override fun navigationView(): NavigationView = navigationMenuController.navigationView

    fun mainConnectionVisibility(visibility: Int) {
        findViewById<View>(R.id.main_connection).visibility = visibility
    }

}