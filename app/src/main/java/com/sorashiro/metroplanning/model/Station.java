package com.sorashiro.metroplanning.model;

import com.sorashiro.metroplanning.ConstantValue;

public class Station extends BlockBase {

    private int x;
    private int y;

    private int passenger;
    private int priority;

    public Station(int x, int y, int color, int passenger) {
        super(ConstantValue.STATION, color, ConstantValue.LAYER_0);
        this.x = x;
        this.y = y;
        this.passenger = passenger;
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

}
