package com.example.android.momintuition;

/**
 * Created by rpodiuc on 10/8/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class GMapListener extends AccessibilityService{
    static final String TAG = "RecorderService";


        @Override
        public void onCreate() {
           Log.i("on create ser", "SERVICE");


        }

    //helper function not used yet
    private String getEventType(AccessibilityEvent event) {
        switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                return "TYPE_NOTIFICATION_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                return "TYPE_VIEW_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                return "TYPE_VIEW_FOCUSED";
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                return "TYPE_VIEW_LONG_CLICKED";
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                return "TYPE_VIEW_SELECTED";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "TYPE_WINDOW_STATE_CHANGED";
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                return "TYPE_VIEW_TEXT_CHANGED";
        }
        return "default";
    }



    public  boolean currentServicesRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            //no such name
            if ("com.google.android.apps.maps".equals(service.service.getClassName())) {
                Log.i("MAPS", "RUNNNING");
                return true;
            }
            Log.i("running services", service.service.getClassName());
        }
        return false;
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            try {
                Log.i("START", "----------------");
                    alternativeRead(event);
                Log.i("END", "---------------");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Parcelable data = event.getParcelableData();
            if (data instanceof Notification) {
                Notification notification = (Notification) data;
                //Log.i("NOTIFICATION","ticker: " + notification.tickerText);
                //Log.i("NOTIFICATION","icon: " + notification.icon);
                Log.i("NOTIFICATION", "notification getText: "+ event.getText());
                //Log.i("NOTIFICATION", "verbose"+ getEventText(event));
            }

        }

    }


    public void alternativeRead(AccessibilityEvent event) throws IllegalAccessException {
        Notification notification = (Notification) event.getParcelableData();
        CharSequence ch = notification.extras.getCharSequence(notification.EXTRA_TEXT);
        CharSequence ch2 = notification.extras.getCharSequence(notification.EXTRA_TITLE);
        if (ch != null)
        Log.i("EXTRATEXT------", ch.toString());
        if (ch2 != null)
            Log.i("EXTRATITLE----", ch2.toString());
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }


}
