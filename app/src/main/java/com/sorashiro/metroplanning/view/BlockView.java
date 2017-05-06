package com.sorashiro.metroplanning.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.sorashiro.metroplanning.ConstantValue;
import com.sorashiro.metroplanning.R;
import com.sorashiro.metroplanning.model.BlockBase;
import com.sorashiro.metroplanning.model.Metro;
import com.sorashiro.metroplanning.model.Station;
import com.sorashiro.metroplanning.model.Turnout;
import com.sorashiro.metroplanning.util.DisplayUtil;
import com.sorashiro.metroplanning.util.Drawable2Bitmap;
import com.sorashiro.metroplanning.util.LogAndToastUtil;

import java.util.HashMap;

/**
 * Created by GameKing on 2017/5/4.
 */

public class BlockView extends View {

    private int width;
    private int height;

    private int blockX;
    private int blockY;
    private int blockZ;

    private Paint     mPaint      = new Paint();
    private Path      mPath       = new Path();
    private TextPaint mWhitePaint = new TextPaint();

    private HashMap<Integer, BlockBase> mBlockBaseHashMap = new HashMap<>();

    public BlockView(Context context) {
        super(context);
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWhitePaint.setColor(getResources().getColor(R.color.white_text));
        mWhitePaint.setTextSize(getResources().getDimension(R.dimen.block_text_size));
        mWhitePaint.setTextAlign(Paint.Align.CENTER);

        int px = DisplayUtil.sp2px(getContext(), getResources().getDimension(R.dimen.block_text_size));
        int padding = px/7;

        canvas.translate(width / 2, height / 2);

        float small = width < height ? width : height;

        for(int i = 0; i <= mBlockBaseHashMap.size(); i++) {
            if(mBlockBaseHashMap.get(i) != null) {
                canvas.save();
                mPath.reset();
                BlockBase blockBase = mBlockBaseHashMap.get(i);
                mPaint.setColor(blockBase.getColor());
                switch (blockBase.getType()) {
                    case ConstantValue.METRO:
                        Metro metro = (Metro) blockBase;

                        switch (metro.getOrientation()) {
                            case ConstantValue.DOWN:
                                canvas.rotate(90);
                                break;
                            case ConstantValue.LEFT:
                                canvas.rotate(180);
                                break;
                            case ConstantValue.UP:
                                canvas.rotate(270);
                                break;
                            case ConstantValue.RIGHT:
                                break;
                        }
                        //Right Orientation
                        mPath.moveTo(-small / 2, -small / 2);
                        mPath.lineTo(small / 2, 0);
                        mPath.lineTo(-small / 2, small / 2);
                        mPath.lineTo(-small / 2, -small / 2);
                        mPath.close();
                        canvas.drawPath(mPath, mPaint);

                        canvas.restoreToCount(1);

                        switch (metro.getOrientation()) {
                            case ConstantValue.DOWN:
                                canvas.drawText(metro.getPassenger()+"", 0, 0, mWhitePaint);
                                break;
                            case ConstantValue.LEFT:
                                canvas.drawText(metro.getPassenger()+"", padding, padding, mWhitePaint);
                                break;
                            case ConstantValue.UP:
                                canvas.drawText(metro.getPassenger()+"", 0, padding, mWhitePaint);
                                break;
                            case ConstantValue.RIGHT:
                                canvas.drawText(metro.getPassenger()+"", -padding, padding, mWhitePaint);
                                break;
                        }
                        break;
                    case ConstantValue.STATION:
                        Station station = (Station) blockBase;

                        mPath.moveTo(-small / 2, -small / 2);
                        mPath.lineTo(small / 2, -small / 2);
                        mPath.lineTo(small / 2, small / 2);
                        mPath.lineTo(-small / 2, small / 2);
                        mPath.close();
                        canvas.drawPath(mPath, mPaint);

                        canvas.restoreToCount(1);

                        String text = station.getPassenger() + "";
                        String priority = station.getPriority() + "";
                        canvas.drawText(text, 0, -px/6+padding, mWhitePaint);
                        canvas.drawText(priority, 0, px/6+padding, mWhitePaint);
                        break;
                    case ConstantValue.TURNOUT:
                        switch (((Turnout) blockBase).getOrientation()) {
                            case ConstantValue.DOWN:
                                canvas.rotate(90);
                                break;
                            case ConstantValue.LEFT:
                                canvas.rotate(180);
                                break;
                            case ConstantValue.UP:
                                canvas.rotate(270);
                                break;
                            case ConstantValue.RIGHT:
                                break;
                        }
                        //Right Orientation
                        mPath.moveTo(-0.43f * small, -0.07f * small);
                        mPath.lineTo(-0.43f * small, 0.07f * small);
                        mPath.lineTo(0.12f * small, 0.07f * small);
                        mPath.lineTo(-0.17f * small, 0.38f * small);
                        mPath.lineTo(0.06f * small, 0.38f * small);
                        mPath.lineTo(0.43f * small, 0);
                        mPath.lineTo(0.06f * small, -0.38f * small);
                        mPath.lineTo(-0.17f * small, -0.38f * small);
                        mPath.lineTo(0.12f * small, -0.07f * small);
                        mPath.close();
                        canvas.drawPath(mPath, mPaint);

                        canvas.restoreToCount(1);
                        break;
                    case ConstantValue.USABLE:
                        continue;
                }
            }
        }
    }

    public void setBlockBase(int layer, BlockBase blockBase) {
        mBlockBaseHashMap.put(layer, blockBase);
        invalidate();
    }

    public BlockBase getBlockBase(int layer){
        return mBlockBaseHashMap.get(layer);
    }

    public void clearBlockBase() {
        mBlockBaseHashMap.clear();
        invalidate();
    }

    public int getBlockX() {
        return blockX;
    }

    public void setBlockX(int blockX) {
        this.blockX = blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public void setBlockY(int blockY) {
        this.blockY = blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public void setBlockZ(int blockZ) {
        this.blockZ = blockZ;
    }
}
