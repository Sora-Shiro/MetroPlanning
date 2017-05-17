package com.sorashiro.metroplanning.model;

import com.sorashiro.metroplanning.ConstantValue;

public class Turnout extends BlockBase {

    private int x;
    private int y;

    private int orientation;

    public Turnout(int x, int y, int color, int orientation) {
        super(ConstantValue.TURNOUT, color, ConstantValue.LAYER_0);
        this.x = x;
        this.y = y;
        this.orientation = orientation;
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

    public void rotateClockwise() {
        switch (orientation) {
            case ConstantValue.UP:
                orientation = ConstantValue.RIGHT;
                break;
            case ConstantValue.DOWN:
                orientation = ConstantValue.LEFT;
                break;
            case ConstantValue.LEFT:
                orientation = ConstantValue.UP;
                break;
            case ConstantValue.RIGHT:
                orientation = ConstantValue.DOWN;
                break;
        }
    }

}
