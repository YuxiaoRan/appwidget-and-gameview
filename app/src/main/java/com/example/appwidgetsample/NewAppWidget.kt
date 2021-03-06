package com.example.appwidgetsample

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import com.example.appwidgetsample.NewAppWidget.Companion.COUNT_KEY
import com.example.appwidgetsample.NewAppWidget.Companion.mSharedPrefFile
import java.text.DateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal val mSharedPrefFile = "com.example.android.appwidgetsample"
        internal val COUNT_KEY = "count"
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val prefs: SharedPreferences = context.getSharedPreferences(mSharedPrefFile, 0)
    var count: Int = prefs.getInt(COUNT_KEY + appWidgetId, 0)
    count++

    val dateString: String = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_id, appWidgetId.toString())
    println("placing widget #" + appWidgetId)

    views.setTextViewText(R.id.appwidget_update, context.resources.getString(
        R.string.date_count_format, count, dateString))

    val prefEditor: SharedPreferences.Editor = prefs.edit()
    prefEditor.putInt(COUNT_KEY + appWidgetId, count)
    prefEditor.apply()

    // intent
    val intentUpdate = Intent(context, NewAppWidget::class.java)
    intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    var idArray: IntArray = intArrayOf(appWidgetId)
    intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)

    val pendingUpdate = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate,
        PendingIntent.FLAG_UPDATE_CURRENT)

    views.setOnClickPendingIntent(R.id.button_update, pendingUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}