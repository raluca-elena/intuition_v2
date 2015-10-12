package com.example.android.momintuition;

/**
 * Created by rpodiuc on 10/8/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;


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



    private String getEventText(AccessibilityEvent event) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence s : event.getText()) {
            sb.append(s);
        }
        return sb.toString();
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Parcelable data = event.getParcelableData();
            if (data instanceof Notification) {
                Notification notification = (Notification) data;
                Log.i("NOTIFICATION","ticker: " + notification.tickerText);
                Log.i("NOTIFICATION","icon: " + notification.icon);
                Log.i("NOTIFICATION", "notification getText: "+ event.getText());
                Log.i("NOTIFICATION", "verbose"+ getEventText(event));
            }

        }
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }


}
