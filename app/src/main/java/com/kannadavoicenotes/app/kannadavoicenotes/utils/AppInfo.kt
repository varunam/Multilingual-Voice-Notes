package com.kannadavoicenotes.app.kannadavoicenotes.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.net.Uri

/**
 * Created by varun.am on 06/12/18
 */
object AppInfo {

    private const val PREF_APP_LAUNCH = "pref-app-launch"
    private const val APP_LAUNCHED_KEY = "app-launched-key"

    fun getAppVersion(context: Context): String {
        var appVersion = "Not Available"
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            appVersion = "v" + packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return appVersion
    }

    fun getAppLinkOnPlayStore(context: Context): Uri {
        return Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
    }

    fun getAppLaunched(context: Context): Boolean {
        val pref = context.getSharedPreferences(PREF_APP_LAUNCH, MODE_PRIVATE)
        return pref.getBoolean(APP_LAUNCHED_KEY, false)
    }

    fun setAppLaunched(context: Context, appLaunched: Boolean) {
        val pref = context.getSharedPreferences(PREF_APP_LAUNCH, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(APP_LAUNCHED_KEY, appLaunched)
        editor.apply()
    }

}