package com.sorashiro.metroplanning.model;

import com.sorashiro.metroplanning.ConstantValue;

/**
 * Created by GameKing on 2017/4/30.
 * 由于Metro的特殊性，使用他的set方法务必在主线程执行
 */

public class Metro extends BlockBase {

    private int x;
    private int y;
    private int orientation;
    private int changeX;
    private int changeY;

    private int     speed;
    private int     tempDelay;
    private boolean driving;

    private int passenger;
    private int fullLoad;
    private int preStationX;
    private int preStationY;


    public Metro(int x, int y, int color, int orientation, int speed, int fullLoad) {
        super(ConstantValue.METRO, color, ConstantValue.LAYER_1);
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.speed = speed;
        this.tempDelay = 0;
        this.driving = true;
        this.passenger = 0;
        this.fullLoad = fullLoad;
        this.preStationX = -1;
        this.preStationY = -1;
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

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getChangeX() {
        return changeX;
    }

    public void setChangeX(int changeX) {
        this.changeX = changeX;
    }

    public int getChangeY() {
        return changeY;
    }

    public void setChangeY(int changeY) {
        this.changeY = changeY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTempDelay() {
        return tempDelay;
    }

    public void setTempDelay(int tempDelay) {
        this.tempDelay = tempDelay;
    }

    public boolean isDriving() {
        return driving;
    }

    public void setDriving(boolean driving) {
        this.driving = driving;
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int passenger) {
        this.passenger = passenger;
    }

    public int getFullLoad() {
        return fullLoad;
    }

    public void setFullLoad(int fullLoad) {
        this.fullLoad = fullLoad;
    }

    public int getPreStationX() {
        return preStationX;
    }

    public void setPreStationX(int preStationX) {
        this.preStationX = preStationX;
    }

    public int getPreStationY() {
        return preStationY;
    }

    public void setPreStationY(int preStationY) {
        this.preStationY = preStationY;
    }

    public void updatePosition(){
        switch (orientation) {
            case ConstantValue.UP:
                changeX = x-1;
                changeY = y;
                break;
            case ConstantValue.DOWN:
                changeX = x+1;
                changeY = y;
                break;
            case ConstantValue.LEFT:
                changeX = x;
                changeY = y-1;
                break;
            case ConstantValue.RIGHT:
                changeX = x;
                changeY = y+1;
                break;
        }
    }

}
