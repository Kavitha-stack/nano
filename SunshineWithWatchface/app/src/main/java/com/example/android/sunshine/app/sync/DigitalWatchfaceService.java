package com.example.android.sunshine.app.sync;

import com.example.android.sunshine.app.sync.SunshineSyncAdapter;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


/**
 * Created by Kavitha
 */
public class DigitalWatchfaceService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/updatesunshine")) {
            SunshineSyncAdapter.syncImmediately(this);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }
}
