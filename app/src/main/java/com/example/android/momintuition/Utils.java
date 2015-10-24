package com.example.android.momintuition;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.animation.Interpolator;

/**
 * Created by rpodiuc on 10/1/15.
 */
public class Utils extends Activity{


    public  String getThemeName()
    {
        PackageInfo packageInfo;
        try
        {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            int themeResId = packageInfo.applicationInfo.theme;
            return getResources().getResourceEntryName(themeResId);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }
}

class MVAccelerateDecelerateInterpolator implements Interpolator {

    // easeInOutQuint
    public float getInterpolation(float t) {
        float x = t*2.0f;
        if (t<0.5f) return 0.5f*x*x*x*x*x;
        x = (t-0.5f)*2-1;
        return 0.5f*x*x*x*x*x+1;
    }
}

