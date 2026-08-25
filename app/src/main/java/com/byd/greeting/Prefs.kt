package com.byd.greeting

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri

object Prefs {
    private const val PREFS_NAME = "byd_greeting_prefs"
    private const val KEY_START_URI = "start_audio_uri"
    private const val KEY_END_URI = "end_audio_uri"
    private const val KEY_START_NAME = "start_audio_name"
    private const val KEY_END_NAME = "end_audio_name"
    private const val KEY_AUTO_START_FLOAT = "auto_start_float"
    private const val KEY_FLOAT_X = "float_x"
    private const val KEY_FLOAT_Y = "float_y"
    private const val KEY_FLOAT_RUNNING = "float_running"
    private const val KEY_LICENSE_ACTIVATED = "license_activated"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getStartUri(context: Context): Uri? {
        val str = prefs(context).getString(KEY_START_URI, null)
        return if (str.isNullOrEmpty()) null else Uri.parse(str)
    }

    fun getEndUri(context: Context): Uri? {
        val str = prefs(context).getString(KEY_END_URI, null)
        return if (str.isNullOrEmpty()) null else Uri.parse(str)
    }

    fun getStartName(context: Context): String {
        return prefs(context).getString(KEY_START_NAME, "Chưa chọn") ?: "Chưa chọn"
    }

    fun getEndName(context: Context): String {
        return prefs(context).getString(KEY_END_NAME, "Chưa chọn") ?: "Chưa chọn"
    }

    fun setStartAudio(context: Context, uri: Uri, name: String) {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        prefs(context).edit()
            .putString(KEY_START_URI, uri.toString())
            .putString(KEY_START_NAME, name)
            .apply()
    }

    fun setEndAudio(context: Context, uri: Uri, name: String) {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        prefs(context).edit()
            .putString(KEY_END_URI, uri.toString())
            .putString(KEY_END_NAME, name)
            .apply()
    }

    fun clearStart(context: Context) {
        prefs(context).edit()
            .remove(KEY_START_URI)
            .remove(KEY_START_NAME)
            .apply()
    }

    fun clearEnd(context: Context) {
        prefs(context).edit()
            .remove(KEY_END_URI)
            .remove(KEY_END_NAME)
            .apply()
    }

    fun isAutoStartFloat(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_AUTO_START_FLOAT, true)
    }

    fun setAutoStartFloat(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_AUTO_START_FLOAT, enabled).apply()
    }

    fun getFloatX(context: Context): Int = prefs(context).getInt(KEY_FLOAT_X, 40)
    fun getFloatY(context: Context): Int = prefs(context).getInt(KEY_FLOAT_Y, 200)

    fun setFloatPosition(context: Context, x: Int, y: Int) {
        prefs(context).edit()
            .putInt(KEY_FLOAT_X, x)
            .putInt(KEY_FLOAT_Y, y)
            .apply()
    }

    fun setFloatRunning(context: Context, running: Boolean) {
        prefs(context).edit().putBoolean(KEY_FLOAT_RUNNING, running).apply()
    }

    fun isFloatRunning(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_FLOAT_RUNNING, false)
    }

    fun isLicenseActivated(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_LICENSE_ACTIVATED, false)
    }

    fun setLicenseActivated(context: Context, activated: Boolean) {
        prefs(context).edit().putBoolean(KEY_LICENSE_ACTIVATED, activated).apply()
    }
}
