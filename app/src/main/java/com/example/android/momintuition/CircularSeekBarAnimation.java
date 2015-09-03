package com.example.android.momintuition;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by rpodiuc on 8/28/15.
 */
    class CircularSeekBarAnimation extends Animation {
        private CircularSeekBar progressBar;
        private float from;
        private float  to;

        public CircularSeekBarAnimation(CircularSeekBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            //Log.i("apply transf", value + "");
            progressBar.setProgress((int) value);
            progressBar.requestLayout();
        }

    }


