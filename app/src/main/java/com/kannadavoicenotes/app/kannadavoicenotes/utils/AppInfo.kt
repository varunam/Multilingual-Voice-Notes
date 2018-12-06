package com.kannadavoicenotes.app.kannadavoicenotes.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri

/**
 * Created by varun.am on 06/12/18
 */
object AppInfo {

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

}