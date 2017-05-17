package com.sorashiro.metroplanning.model;

import com.sorashiro.metroplanning.ConstantValue;

public class Usable extends BlockBase {

    private int x;
    private int y;

    public Usable(int x, int y, int color) {
        super(ConstantValue.USABLE, color, ConstantValue.LAYER_0);
        this.x = x;
        this.y = y;
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
}
