/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Digital watch face. In ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class DigitalWatchface extends CanvasWatchFaceService {

    /**
     * Active mode update rate
     */
    private static final long ACTIVE_INTERVAL_MS = TimeUnit.SECONDS.toMillis(1);


    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


        private static final String COLON_STRING = ":";
        private static final int MSG_UPDATE_TIME = 0;
        private static final int TEXT_TIME_COLOR = Color.WHITE;
        private static final int TEXT_APM_COLOR = Color.WHITE;
        private static final int TEXT_COLON_COLOR = Color.WHITE;

        // To update in interactive mode.
        private final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (shouldUpdateTimeHandlerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs =
                                    ACTIVE_INTERVAL_MS - (timeMs % ACTIVE_INTERVAL_MS);
                            mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };



        private String amString;
        private String pmString;
        private float timeYOffset;
        private float iconYOffset;
        private float temperatureYOffset;
        private double iconMultiplier;
        private boolean ambientMode = false;
        private String highTemperature;
        private String lowTemperature;
        private int weatherId;
        GoogleApiClient googleClient;
        private Paint hourPaint;
        private Paint minutePaint;
        private Paint amPmPaint;
        private Paint colonPaint;
        private Paint iconPaint;
        private Paint highPaint;
        private Paint lowPaint;
        private float colonWidth;
        private Calendar calendar;
        private boolean lowBitAmbient;

        @Override
        public void onConnected(Bundle bundle) {
        }

        @Override
        public void onConnectionSuspended(int i) {
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
        }


        public class MessageReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getExtras() != null && intent.getExtras().containsKey(getString(R.string.extraMsg))) {
                    String message = intent.getStringExtra(getString(R.string.extraMsg));
                    String[] splitMessage = message.split("\\s+");
                    highTemperature = splitMessage[0];
                    lowTemperature = splitMessage[1];
                    weatherId = Integer.parseInt(splitMessage[2]);
                } else {
                    calendar.setTimeZone(TimeZone.getDefault());
                }
                invalidate();
            }
        }

        @Override
        public void onCreate(SurfaceHolder holder) {
            googleClient = new GoogleApiClient.Builder(DigitalWatchface.this)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            googleClient.connect();
            super.onCreate(holder);


            setWatchFaceStyle(new WatchFaceStyle.Builder(DigitalWatchface.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            Resources resources = getResources();
            amString = resources.getString(R.string.wear_am);
            pmString = resources.getString(R.string.wear_pm);

            hourPaint = createTextPaint(TEXT_TIME_COLOR, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
            minutePaint = createTextPaint(TEXT_TIME_COLOR);
            amPmPaint = createTextPaint(TEXT_APM_COLOR);
            colonPaint = createTextPaint(TEXT_COLON_COLOR);
            highPaint = createTextPaint(Color.WHITE, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
            lowPaint = createTextPaint(Color.WHITE);


            iconPaint = new Paint();
            iconPaint.setColor(Color.BLACK);

            calendar = Calendar.getInstance();

            IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
            MessageReceiver messageReceiver = new MessageReceiver();
            LocalBroadcastManager.getInstance(DigitalWatchface.this)
                    .registerReceiver(messageReceiver, messageFilter);

        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            googleClient.disconnect();
            super.onDestroy();

        }

        private Paint createTextPaint(int color, Typeface typeface) {
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setTypeface(typeface);
            paint.setAntiAlias(true);
            return paint;
        }

        private Paint createTextPaint(int color) {
            return createTextPaint(color, Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        }

        @Override
        public void onVisibilityChanged(boolean visible) {


            super.onVisibilityChanged(visible);

            if (visible) {
                calendar.setTimeZone(TimeZone.getDefault());
            }
            updateTimer();
        }


        @Override
        public void onApplyWindowInsets(WindowInsets insets) {


            super.onApplyWindowInsets(insets);

            // Load resources for round watches.
            Resources resources = DigitalWatchface.this.getResources();
            boolean isRound = insets.isRound();

            float textSize = resources.getDimension(isRound
                    ? R.dimen.weather_text_size_round : R.dimen.weather_text_size);
            float amPmSize = resources.getDimension(isRound
                    ? R.dimen.weather_am_pm_size_round : R.dimen.weather_am_pm_size);

            timeYOffset = resources.getDimension(isRound
                    ? R.dimen.time_y_offset_round : R.dimen.time_y_offset);
            iconYOffset = resources.getDimension(isRound
                    ? R.dimen.icon_y_offset_round : R.dimen.icon_y_offset);
            temperatureYOffset = resources.getDimension(isRound
                    ? R.dimen.temperature_y_offset_round : R.dimen.temperature_y_offset);

            iconMultiplier = isRound ? 1.5 : 1;


            hourPaint.setTextSize(textSize);
            minutePaint.setTextSize(textSize);
            amPmPaint.setTextSize(amPmSize);
            colonPaint.setTextSize(textSize);
            highPaint.setTextSize(textSize);
            lowPaint.setTextSize(amPmSize);
            colonWidth = colonPaint.measureText(COLON_STRING);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            boolean burnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);

            //Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

            hourPaint.setTypeface(burnInProtection ? Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL) : Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

            lowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);

        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            ambientMode = inAmbientMode;
            if (lowBitAmbient) {
                boolean antiAlias = !inAmbientMode;
                hourPaint.setAntiAlias(antiAlias);
                minutePaint.setAntiAlias(antiAlias);
                amPmPaint.setAntiAlias(antiAlias);
                colonPaint.setAntiAlias(antiAlias);
                highPaint.setAntiAlias(antiAlias);
                lowPaint.setAntiAlias(antiAlias);
            }
            invalidate();
            updateTimer();
        }

        private String formatTwoDigitNumber(int hour) {
            return String.format("%02d", hour);
        }

        private String getAmPmString(int amPm) {
            return amPm == Calendar.AM ? amString : pmString;
        }

        Bitmap scaledBackground;
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            if (highTemperature == null) {
                new SendToDataLayerThread("/updatesunshine").start();
            }
            long now = System.currentTimeMillis();
            calendar.setTimeInMillis(now);
            boolean is24Hour = DateFormat.is24HourFormat(DigitalWatchface.this);

            Drawable drawables = getResources().getDrawable(R.drawable.bg,null);
            Bitmap backgroundBitmap = ((BitmapDrawable) drawables).getBitmap();
            int width = bounds.width();
            int height = bounds.height();


            if(scaledBackground == null || scaledBackground.getWidth()!=width || scaledBackground.getHeight() != height){
                scaledBackground = Bitmap.createScaledBitmap(backgroundBitmap,width,height,true);
            }

            // Draw the background.
            if (ambientMode) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawBitmap(scaledBackground, 0, 0, null);
               // canvas.drawColor(getColor(R.color.primary));
            }


            float newX = 0;
            String hourString;

            // Build Strings
            if (is24Hour) {
                hourString = formatTwoDigitNumber(calendar.get(Calendar.HOUR_OF_DAY));
            } else {
                int hour = calendar.get(Calendar.HOUR);
                if (hour == 0) {
                    hour = 12;
                }
                hourString = String.valueOf(hour);
            }

            String minuteString = formatTwoDigitNumber(calendar.get(Calendar.MINUTE));
            String amPmString = getAmPmString(calendar.get(Calendar.AM_PM));
            newX += hourPaint.measureText(hourString);
            newX += colonWidth;
            newX += minutePaint.measureText(minuteString);
            if (!is24Hour) {
                newX += hourPaint.measureText(hourString);
            }
            float x = bounds.centerX() - newX / 2;
            float xTemperature = 0;
            float xIcon = 0;
            if (highTemperature != null && lowTemperature != null) {
                xTemperature = bounds.centerX() - (highPaint.measureText(
                        highTemperature + " " + lowTemperature) / 2);
            }

            canvas.drawText(hourString, x, timeYOffset, hourPaint);
            x += hourPaint.measureText(hourString);

            canvas.drawText(COLON_STRING, x, timeYOffset, colonPaint);
            x += colonWidth;

            canvas.drawText(minuteString, x, timeYOffset, minutePaint);
            x += minutePaint.measureText(minuteString);


            if (!is24Hour) {
                x += colonWidth;
                canvas.drawText(amPmString, x, timeYOffset, amPmPaint);
            }

            if (getPeekCardPosition().isEmpty() && !ambientMode) {
                if (highTemperature != null && lowTemperature != null) {
                    canvas.drawText(
                            highTemperature,
                            xTemperature,
                            temperatureYOffset,
                            highPaint);

                    xTemperature += highPaint.measureText(highTemperature + " ");

                    canvas.drawText(lowTemperature,
                            xTemperature,
                            temperatureYOffset,
                            lowPaint);
                }
                if (weatherId > 0) {
                    Drawable drawable = getResources().getDrawable(getIconResourceForWeatherCondition(weatherId));
                    Bitmap weatherIcon = ((BitmapDrawable) drawable).getBitmap();

                    Bitmap scaledWeatherIcon =
                            Bitmap.createScaledBitmap(weatherIcon,
                                    (int) (iconMultiplier * highPaint.getTextSize()),
                                    (int) (iconMultiplier * highPaint.getTextSize()), true);

                    canvas.drawBitmap(scaledWeatherIcon,
                            bounds.centerY() - scaledWeatherIcon.getHeight() / 2,
                            iconYOffset,
                            null);
                }

            }


        }

        public int getIconResourceForWeatherCondition(int weatherId) {
            // Weather_Condition_Codes
            if (weatherId >= 200 && weatherId <= 232) {
                return R.drawable.ic_storm;
            } else if (weatherId >= 300 && weatherId <= 321) {
                return R.drawable.ic_light_rain;
            } else if (weatherId >= 500 && weatherId <= 504) {
                return R.drawable.ic_rain;
            } else if (weatherId == 511) {
                return R.drawable.ic_snow;
            } else if (weatherId >= 520 && weatherId <= 531) {
                return R.drawable.ic_rain;
            } else if (weatherId >= 600 && weatherId <= 622) {
                return R.drawable.ic_snow;
            } else if (weatherId >= 701 && weatherId <= 761) {
                return R.drawable.ic_fog;
            } else if (weatherId == 761 || weatherId == 781) {
                return R.drawable.ic_storm;
            } else if (weatherId == 800) {
                return R.drawable.ic_clear;
            } else if (weatherId == 801) {
                return R.drawable.ic_light_clouds;
            } else if (weatherId >= 802 && weatherId <= 804) {
                return R.drawable.ic_cloudy;
            }
            return -1;
        }

        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldUpdateTimeHandlerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        private boolean shouldUpdateTimeHandlerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        class SendToDataLayerThread extends Thread {
            String path;
            String message;

            // Constructor to send a message to the data layer
            SendToDataLayerThread(String p) {
                path = p;
                message = "updater";
            }

            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleClient).await();
                for (Node node : nodes.getNodes()) {
                     Wearable.MessageApi.sendMessage(googleClient, node.getId(), path, message.getBytes()).await();
                }
            }
        }


    }
}