package com.sorashiro.metroplanning.model;

import android.graphics.drawable.Drawable;

import com.sorashiro.metroplanning.ConstantValue;

/**
 * Created by GameKing on 2017/5/1.
 */

public class Turnout extends BlockBase {

    private int x;
    private int y;

    private int orientation;

    private Drawable[] turnoutDrawable;

    public Turnout(int x, int y, int color, int orientation, Drawable[] turnoutDrawable) {
        super(ConstantValue.TURNOUT, color, ConstantValue.LAYER_0);
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.turnoutDrawable = turnoutDrawable;

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

    public Drawable[] getTurnoutDrawable() {
        return turnoutDrawable;
    }

    public void setTurnoutDrawable(Drawable[] turnoutDrawable) {
        this.turnoutDrawable = turnoutDrawable;
    }
}
