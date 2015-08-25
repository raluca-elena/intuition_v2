package com.example.android.momintuition;

import android.graphics.RectF;

/**
 * Created by rpodiuc on 8/7/15.
 */
public class MultiCircularSeekbar {

    protected int dividew = 1;
    protected int divideh = 1;
    protected float innerRadius;

    /**
     * The radius of the outer circle
     */
    protected float outerRadius;

    /**
     * The left bound for the circle RectF
     */
    protected float left;

    /**
     * The right bound for the circle RectF
     */
    protected float right;
    /**
     * The top bound for the circle RectF
     */
    protected float top;

    /**
     * The bottom bound for the circle RectF
     */
    protected float bottom;

    /**
     * The X coordinate for the top left corner of the marking drawable
     */
    protected float dx;

    /**
     * The Y coordinate for the top left corner of the marking drawable
     */
    protected float dy;

    /**
     * The X coordinate for 12 O'Clock
     */
    protected float startPointX;

    /**
     * The Y coordinate for 12 O'Clock
     */
    protected float startPointY;


    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    protected float markPointX;

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    protected float markPointY;

    protected RectF rect = new RectF();
}

