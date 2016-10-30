package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kavitha on 10/28/2016.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor mCursor;
    public static Context mContext;
    private int mAppWidgetId;
    private int layoutId;

    public StackRemoteViewsFactory(Context context, Intent intent, int layout) {
        mContext = context;
        layoutId = layout;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        Uri uri = QuoteProvider.Quotes.CONTENT_URI;
        try {
            //   mCursor = mContext.getContentResolver().query(uri,new String[] { "Distinct " + QuoteColumns.SYMBOL }, null, null, null);
            mCursor = mContext.getContentResolver().query(uri, WidgetService.STOCK_COLUMNS, null, null, null);
        } catch (Exception ex) {
            System.out.println("ex:" + ex.getMessage());
        }
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), layoutId);
        mCursor.moveToPosition(position);
        rv.setTextViewText(R.id.symbol,
                mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)).toUpperCase());
        rv.setTextViewText(R.id.change,
                mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction(WidgetProvider.ACTION_TOAST);
        final Bundle bundle = new Bundle();
        bundle.putString(WidgetProvider.EXTRA_STRING, "Change for " +
                mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)) + " is " + mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));
        fillInIntent.putExtras(bundle);
        fillInIntent.putExtra(mContext.getString(R.string.symbol), mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL)).toUpperCase());
        rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
        return rv;
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();


        Uri uri = QuoteProvider.Quotes.CONTENT_URI;
        Cursor data = mContext.getContentResolver().query(uri, WidgetService.STOCK_COLUMNS, null,
                null, null);
        mCursor = data;
        Binder.restoreCallingIdentity(identityToken);

    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}