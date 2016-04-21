package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.CursorRecyclerViewAdapter;
import com.sam_chordas.android.stockhawk.rest.QuoteCursorAdapter;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;
import com.sam_chordas.android.stockhawk.ui.StockWidget;

/**
 * Created by vic on 20/04/2016.
 */
public class WidgetIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetIntentService(String name) {
        super("WidgetIntentService");
    }

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockWidget.class));
        QuoteCursorAdapter mCursorAdapter = new QuoteCursorAdapter(this, null);

        Cursor cursos = this.getApplicationContext().getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{"Distinct " + QuoteColumns.SYMBOL + ", " + QuoteColumns.BIDPRICE}, null,
                null, null);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.stock_widget);
        if (cursos != null) {
            cursos.moveToFirst();
            String symbol = String.valueOf(cursos.getString(cursos.getColumnIndex("symbol"))); // bid_price
            String value = String.valueOf(cursos.getString(cursos.getColumnIndex("bid_price")));
            views.setTextViewText(R.id.appwidget_text, symbol);
            views.setTextViewText(R.id.value, value);
        } else
            views.setTextViewText(R.id.appwidget_text, "loading ...");
        Intent intentApp = new Intent(this, MyStocksActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentApp, 0);
        views.setOnClickPendingIntent(R.id.containerRL, pendingIntent);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
