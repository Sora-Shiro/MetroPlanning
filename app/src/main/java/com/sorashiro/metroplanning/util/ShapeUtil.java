package com.sorashiro.metroplanning.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * @author Sora
 * @date 2017.5.1
 * <p>
 * Shape util class, can generate some Shape, ShapeDrawable, and ShapeDrawable-array.
 * Shape工具类，能生成Shape, ShapeDrawable, and ShapeDrawable-array。
 */

public class ShapeUtil {

    public static ShapeDrawable getRoundDrawable(int color) {
        ShapeDrawable d = new ShapeDrawable(getRoundShape());
        d.getPaint().setColor(color);
        return d;
    }

    public static Shape getRoundShape() {
        Shape shape = new Shape() {
            float w;
            float h;
            Path path = new Path();

            @Override
            protected void onResize(float width, float height) {
                this.w = width;
                this.h = height;
            }

            @Override
            public void draw(Canvas canvas, Paint paint) {
                canvas.translate(w / 2, h / 2);
                path.reset();
                float small = w < h ? w : h;
                path.addCircle(0, 0, small/2, Path.Direction.CW);
                path.close();
                canvas.drawPath(path, paint);
            }
        };
        return shape;
    }

    public static ShapeDrawable[] getArrows(int color) {
        ShapeDrawable aUp = ShapeUtil.getArrowDrawable("up", color);
        ShapeDrawable aDown = ShapeUtil.getArrowDrawable("down", color);
        ShapeDrawable aLeft = ShapeUtil.getArrowDrawable("left", color);
        ShapeDrawable aRight = ShapeUtil.getArrowDrawable("right", color);
        ShapeDrawable[] shapeDrawables = new ShapeDrawable[4];
        shapeDrawables[0] = aUp;
        shapeDrawables[1] = aDown;
        shapeDrawables[2] = aLeft;
        shapeDrawables[3] = aRight;
        return shapeDrawables;
    }

    public static ShapeDrawable getArrowDrawable(String orientation, int color) {
        ShapeDrawable d = new ShapeDrawable(getArrowShape(orientation));
        d.getPaint().setColor(color);
        return d;
    }

    public static Shape getArrowShape(final String orientation) {
        Shape shape = new Shape() {
            float w;
            float h;
            Path path = new Path();

            @Override
            protected void onResize(float width, float height) {
                this.w = width;
                this.h = height;
            }

            @Override
            public void draw(Canvas canvas, Paint paint) {
                canvas.translate(w / 2, h / 2);
                switch (orientation) {
                    case "down":
                        canvas.rotate(90);
                        break;
                    case "left":
                        canvas.rotate(180);
                        break;
                    case "up":
                        canvas.rotate(270);
                        break;
                    case "right":
                        break;
                }
                //Right Orientation
                path.reset();
                float small = w < h ? w : h;
                path.moveTo(-0.43f * small, -0.07f * small);
                path.lineTo(-0.43f * small, 0.07f * small);
                path.lineTo(0.12f * small, 0.07f * small);
                path.lineTo(-0.17f * small, 0.38f * small);
                path.lineTo(0.06f * small, 0.38f * small);
                path.lineTo(0.43f * small, 0);
                path.lineTo(0.06f * small, -0.38f * small);
                path.lineTo(-0.17f * small, -0.38f * small);
                path.lineTo(0.12f * small, -0.07f * small);
                path.close();
                canvas.drawPath(path, paint);
            }
        };
        return shape;
    }

    public static ShapeDrawable getRectangleDrawable(int color) {
        ShapeDrawable d = new ShapeDrawable(getRectangleShape());
        d.getPaint().setColor(color);
        return d;
    }

    public static Shape getRectangleShape() {
        Shape shape = new Shape() {
            float w;
            float h;
            Path path = new Path();

            @Override
            protected void onResize(float width, float height) {
                this.w = width;
                this.h = height;
            }

            @Override
            public void draw(Canvas canvas, Paint paint) {
                canvas.translate(w / 2, h / 2);
                path.reset();
                float small = w < h ? w : h;
                path.moveTo(-small / 2, -small / 2);
                path.lineTo(small / 2, -small / 2);
                path.lineTo(small / 2, small / 2);
                path.lineTo(-small / 2, small / 2);
                path.close();
                canvas.drawPath(path, paint);
            }
        };
        return shape;
    }

    public static ShapeDrawable[] getRegTris(int color) {
        ShapeDrawable triUp = ShapeUtil.getRegularTriangleDrawable("up", color);
        ShapeDrawable triDown = ShapeUtil.getRegularTriangleDrawable("down", color);
        ShapeDrawable triLeft = ShapeUtil.getRegularTriangleDrawable("left", color);
        ShapeDrawable triRight = ShapeUtil.getRegularTriangleDrawable("right", color);
        ShapeDrawable[] shapeDrawables = new ShapeDrawable[4];
        shapeDrawables[0] = triUp;
        shapeDrawables[1] = triDown;
        shapeDrawables[2] = triLeft;
        shapeDrawables[3] = triRight;
        return shapeDrawables;
    }

    public static ShapeDrawable getRegularTriangleDrawable(String orientation, int color) {
        ShapeDrawable d = new ShapeDrawable(getRegularTriangleShape(orientation));
        d.getPaint().setColor(color);
        return d;
    }

    public static Shape getRegularTriangleShape(final String orientation) {
        Shape shape = new Shape() {
            float w;
            float h;
            Path path = new Path();

            @Override
            protected void onResize(float width, float height) {
                this.w = width;
                this.h = height;
            }

            @Override
            public void draw(Canvas canvas, Paint paint) {
                canvas.translate(w / 2, h / 2);
                switch (orientation) {
                    case "down":
                        canvas.rotate(90);
                        break;
                    case "left":
                        canvas.rotate(180);
                        break;
                    case "up":
                        canvas.rotate(270);
                        break;
                    case "right":
                        break;
                }
                //Right Orientation
                path.reset();
                float small = w < h ? w : h;
                path.moveTo(-small / 2, -small / 2);
                path.lineTo(small / 2, 0);
                path.lineTo(-small / 2, small / 2);
                path.lineTo(-small / 2, -small / 2);
                path.close();
                canvas.drawPath(path, paint);
            }
        };
        return shape;
    }

    public static ShapeDrawable getNothingDrawable() {
        Shape shape = new Shape() {
            float w;
            float h;
            Path path = new Path();

            @Override
            protected void onResize(float width, float height) {
                this.w = width;
                this.h = height;
            }

            @Override
            public void draw(Canvas canvas, Paint paint) {

            }
        };
        ShapeDrawable d = new ShapeDrawable(shape);
        return d;
    }

}
