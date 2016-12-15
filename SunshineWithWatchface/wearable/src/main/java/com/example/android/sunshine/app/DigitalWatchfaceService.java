package com.example.android.sunshine.app;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Kavitha on 12/9/2016.
 */

    public class DigitalWatchfaceService extends WearableListenerService {
        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
            if (messageEvent.getPath().equals("/message_path")) {
                Intent msgIntent = new Intent();
                msgIntent.setAction(Intent.ACTION_SEND);
                final String msg = new String(messageEvent.getData());
                msgIntent.putExtra(getString(R.string.extraMsg), msg);
                LocalBroadcastManager.getInstance(this).sendBroadcast(msgIntent);
            } else {
                super.onMessageReceived(messageEvent);
            }
        }
    }

