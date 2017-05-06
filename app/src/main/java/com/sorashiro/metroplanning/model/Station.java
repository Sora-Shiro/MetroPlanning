package com.sorashiro.metroplanning.model;

import android.graphics.drawable.Drawable;

import com.sorashiro.metroplanning.ConstantValue;

/**
 * Created by GameKing on 2017/5/1.
 */

public class Station extends BlockBase {

    private int x;
    private int y;

    private int passenger;
    private int priority;

    private Drawable stationDrawable;

    public Station(int x, int y, int color, int passenger, Drawable drawable) {
        super(ConstantValue.STATION, color, ConstantValue.LAYER_0);
        this.x = x;
        this.y = y;
        this.passenger = passenger;
        this.stationDrawable = drawable;
        this.priority = 1;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int passenger) {
        this.passenger = passenger;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Drawable getStationDrawable() {
        return stationDrawable;
    }

    public void setStationDrawable(Drawable stationDrawable) {
        this.stationDrawable = stationDrawable;
    }
}
