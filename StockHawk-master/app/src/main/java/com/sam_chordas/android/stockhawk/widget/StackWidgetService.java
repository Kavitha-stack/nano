package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by Kavitha on 10/28/2016.
 */

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(WidgetService.mContext, intent, R.layout.widget_list_item);
    }

}



