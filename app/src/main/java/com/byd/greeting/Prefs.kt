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
    private const val KEY_SHOW_POPUP = "show_popup_on_open"

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

    /** Mặc định bật popup khi mở app */
    fun isShowPopup(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_SHOW_POPUP, true)
    }

    fun setShowPopup(context: Context, enabled: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_SHOW_POPUP, enabled)
            .apply()
    }
}
