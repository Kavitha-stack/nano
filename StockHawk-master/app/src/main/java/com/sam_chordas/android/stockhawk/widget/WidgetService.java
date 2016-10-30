package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDatabase;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.Stock;
import com.sam_chordas.android.stockhawk.ui.DetailActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kavitha on 10/21/2016.
 */

public class WidgetService extends IntentService {

    private int layoutId;
    public static Context mContext;

    public static final String[] STOCK_COLUMNS = {
            QuoteColumns._ID,
            QuoteColumns.CHANGE,
            QuoteColumns.BIDPRICE,
            QuoteColumns.SYMBOL
    };
    // these indices must match the projection
    private static final int INDEX_ID = 0;
    private static final int INDEX_CHANGE = 1;
    private static final int INDEX_BIDPRICE = 2;
    private static final int INDEX_SYMBOL = 3;

    public WidgetService() {
        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext =getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                WidgetProvider.class));
        Uri uri = QuoteProvider.Quotes.CONTENT_URI;
        Cursor data = getContentResolver().query(uri, STOCK_COLUMNS, null,
                null,null);
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }
        int weatherArtResourceId =  R.drawable.stock_small;
        // Perform this loop procedure for each  widget
        for (int appWidgetId : appWidgetIds) {
            int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_stock_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_stock_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                layoutId = R.layout.widget_stock_large;
                RemoteViews views = new RemoteViews(getPackageName(), layoutId);
                Intent svcIntent = new Intent(this, StackWidgetService.class);
                svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                views.setRemoteAdapter(appWidgetId, R.id.widget_list,
                        svcIntent);
                views.setEmptyView(R.id.widget_list,R.id.widget_empty);

                // Adding collection list item handler
                final Intent onItemClick = new Intent(mContext, WidgetProvider.class);
                onItemClick.setAction(WidgetProvider.ACTION_TOAST);
                onItemClick.setData(Uri.parse(onItemClick
                        .toUri(Intent.URI_INTENT_SCHEME)));
                final PendingIntent onClickPendingIntent = PendingIntent
                        .getBroadcast(mContext, 0, onItemClick,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                views.setPendingIntentTemplate(R.id.widget_list,
                        onClickPendingIntent);



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, "description");
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_list);

            } else if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.widget_stock_normal;

                RemoteViews views = new RemoteViews(getPackageName(), layoutId);
                Intent svcIntent = new Intent(this, StackWidgetService.class);
                svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                views.setRemoteAdapter(appWidgetId, R.id.widget_list,
                        svcIntent);
                views.setEmptyView(R.id.widget_list,R.id.widget_empty);


                Intent launchIntent = new Intent(this, MyStocksActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, "description");
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_list);
            } else {

                layoutId = R.layout.widget_stock_small;
                RemoteViews views = new RemoteViews(getPackageName(), layoutId);
                views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
                Intent launchIntent = new Intent(this, MyStocksActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views, "description");
                }
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
            this.layoutId = layoutId;
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget_icon, description);
    }

    private int getWidgetWidth(AppWidgetManager appWidgetManager, int appWidgetId) {
        // Prior to Jelly Bean, widgets were always their default size
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return getResources().getDimensionPixelSize(R.dimen.widget_stock_default_width);
        }
        // For Jelly Bean and higher devices, widgets can be resized - the current size can be
        // retrieved from the newly added App Widget Options
        return getWidgetWidthFromOptions(appWidgetManager, appWidgetId);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int getWidgetWidthFromOptions(AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            int minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            // The width returned is in dp, but we'll convert it to pixels to match the other widths
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, minWidthDp,
                    displayMetrics);
        }
        return  getResources().getDimensionPixelSize(R.dimen.widget_stock_default_width);
    }
}