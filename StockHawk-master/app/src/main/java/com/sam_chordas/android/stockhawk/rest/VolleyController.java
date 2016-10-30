package com.sam_chordas.android.stockhawk.rest;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Kavitha on 10/23/2016.
 */
public class VolleyController extends Application {

    private static Context context;
    private static VolleyController sInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = new VolleyController();
    }

    public static synchronized VolleyController getInstance(Context c) {
        context = c;
        if (sInstance == null) {
            sInstance = new VolleyController();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }


}
