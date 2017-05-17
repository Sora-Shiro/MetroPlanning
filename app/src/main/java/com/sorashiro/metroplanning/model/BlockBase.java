package com.sorashiro.metroplanning.model;

/**
 * Created by GameKing on 2017/5/1.
 */

public abstract class BlockBase {

    private int type;
    private int color;
    private int layer;

    public BlockBase(int type, int color, int layer){
        this.type = type;
        this.color = color;
        this.layer = layer;
    }

    public abstract int getX();
    public abstract void setX(int x);
    public abstract int getY();
    public abstract void setY(int y);

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
