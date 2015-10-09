package com.example.android.momintuition;

/**
 * Created by rpodiuc on 10/8/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


public class GMapListener extends AccessibilityService{
    static final String TAG = "RecorderService";


        @Override
        public void onCreate() {
//         getServiceInfo().flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
           Log.i("on create ser", "SERVICE");
        }





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
        Log.i("GOT AN EVENT", event+"");
        Log.i("EVENT", String.format(
                "onAccessibilityEvent: [type] %s [class] %s [package] %s [time] %s [text] %s",
                getEventType(event), event.getClassName(), event.getPackageName(),
                event.getEventTime(), getEventText(event)));


        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Log.d("TORTUGA","Recieved event");
            Parcelable data = event.getParcelableData();
            if (data instanceof Notification) {
                Log.i("Tortuga","Recieved notification");
                Notification notification = (Notification) data;
                Log.i("Tortuga","ticker: " + notification.tickerText);
                Log.i("Tortuga","icon: " + notification.icon);
                Log.i("Tortuga", "notification: "+ event.getText());
            }

            if (getEventType(event) == "default"){
                Log.i("IACA", "EVRICA");
                Parcelable data1 = event.getParcelableData();
                Log.i("DATA1", data1+"");




            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.v(TAG, "onInterrupt");
    }

    /*@Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i("TORTUGA", "onServiceConnected" + " " + getServiceInfo());

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        //info.flags = AccessibilityServiceInfo.DEFAULT;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.DEFAULT;


        //info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        //info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

        //info.packageNames = new String[]
        //        { "com.google.android.talk", "com.google.android.apps.maps", };

        //info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;

        //info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }*/
/*
    @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {
            Log.i("EVENT", event.toString());
            //AccessibilityNodeInfo nodeInfo = event.getSource();
            //nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            //nodeInfo.recycle();

            final int eventType = event.getEventType();
            String eventText = null;
            switch(eventType) {
                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                    eventText = "Focused: ";
                    break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                    eventText = "Focused: ";
                    break;
            }

            eventText = eventText + event.getContentDescription();

            // Do something nifty with this text, like speak the composed string
            // back to the user.
            //speakToUser(eventText);

            Log.i("SPeak", eventText + "");


        }

        @Override
        public void onInterrupt() {
            Log.i("onInterr", "user did something");
        }



    @Override
    protected void onServiceConnected()
    {
        Log.i("AAAAAAAAAAA", "ServiceConnected");
        try
        {
            AccessibilityServiceInfo info = new AccessibilityServiceInfo();


            info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                    AccessibilityEvent.TYPE_VIEW_FOCUSED;

            info.packageNames = new String[]
                    { "com.google.android.talk", "com.google.android.apps.maps", };

            Log.i("my info", info.toString() + "");

            info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;

            info.notificationTimeout = 100;

            setServiceInfo(info);
            this.setServiceInfo(info);
        }
        catch(Exception e)
        {
            Log.d("ERROR Connected", e.toString());
        }
    }*/



}
