package com.byd.greeting

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class GreetingWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_PLAY_START = "com.byd.greeting.ACTION_PLAY_START"
        const val ACTION_PLAY_END = "com.byd.greeting.ACTION_PLAY_END"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_PLAY_START -> {
                val uri = Prefs.getStartUri(context)
                AudioPlayer.play(context, uri, "Khởi động")
            }
            ACTION_PLAY_END -> {
                val uri = Prefs.getEndUri(context)
                AudioPlayer.play(context, uri, "Kết thúc")
            }
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_greeting)

        // Start button
        val startIntent = Intent(context, GreetingWidgetProvider::class.java).apply {
            action = ACTION_PLAY_START
        }
        val startPending = PendingIntent.getBroadcast(
            context,
            0,
            startIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btn_start, startPending)

        // End button
        val endIntent = Intent(context, GreetingWidgetProvider::class.java).apply {
            action = ACTION_PLAY_END
        }
        val endPending = PendingIntent.getBroadcast(
            context,
            1,
            endIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.btn_end, endPending)

        // Open settings when clicking title
        val settingsIntent = Intent(context, MainActivity::class.java)
        val settingsPending = PendingIntent.getActivity(
            context,
            2,
            settingsIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_title, settingsPending)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
