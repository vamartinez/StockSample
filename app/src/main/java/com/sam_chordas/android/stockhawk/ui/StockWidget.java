package com.sam_chordas.android.stockhawk.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.service.WidgetIntentService;

/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // CharSequence widgetText = context.getString(R.string.appwidget_text);
        //    CharSequence widgetText = "texto 2";
        // Construct the RemoteViews object
        //    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);

        context.startService(new Intent(context, WidgetIntentService.class));
        //   loadInfo(context, views);
        // appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.e("WIDget", "update!!!!" + appWidgetId);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, WidgetIntentService.class));
    }

    private static void loadInfo(Context context, RemoteViews views) {
        Intent intent = new Intent(context, MyStocksActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.containerRL, pendingIntent);
        QuoteCursorAdapter mCursorAdapter = new QuoteCursorAdapter(context, null);
        Cursor cursos = mCursorAdapter.getCursor();
        if (cursos != null) {
            cursos.moveToFirst();
            String symbol = String.valueOf(cursos.getString(cursos.getColumnIndex("symbol")));
            Log.e("title", symbol);
            views.setTextViewText(R.id.appwidget_text, symbol);
            // Instruct the widget manager to update the widget
        } else
            views.setTextViewText(R.id.appwidget_text, "loading");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("WIDget", "onReceive 0");
        context.startService(new Intent(context, WidgetIntentService.class));
        if (StockTaskService.ACTION_UPDATE.equals(intent.getAction())) {
            Log.e("WIDget", "onReceive");

            //   context.startService(new Intent(context,))
        }
        //  super.onReceive(context, intent);
    }
}

