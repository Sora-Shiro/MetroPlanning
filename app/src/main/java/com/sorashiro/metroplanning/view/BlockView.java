package com.sorashiro.metroplanning.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sorashiro.metroplanning.ConstantValue;
import com.sorashiro.metroplanning.model.BlockBase;
import com.sorashiro.metroplanning.model.Metro;
import com.sorashiro.metroplanning.model.Station;
import com.sorashiro.metroplanning.model.Turnout;
import com.sorashiro.metroplanning.util.Drawable2Bitmap;
import com.sorashiro.metroplanning.util.LogAndToastUtil;

import java.util.HashMap;

/**
 * Created by GameKing on 2017/5/4.
 */

public class BlockView extends TextView {

    private int width;
    private int height;

    private int blockX;
    private int blockY;
    private int blockZ;

    private Paint mPaint = new Paint();

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

        for(int i = 0; i <= mBlockBaseHashMap.size(); i++) {
            if(mBlockBaseHashMap.get(i) != null) {
                BlockBase blockBase = mBlockBaseHashMap.get(i);
                Drawable drawable = null;
                switch (blockBase.getType()) {
                    case ConstantValue.METRO:
                        drawable = ((Metro) blockBase).getMetroDrawable()[((Metro) blockBase).getOrientation()];
                        break;
                    case ConstantValue.STATION:
                        drawable = ((Station) blockBase).getStationDrawable();
                        break;
                    case ConstantValue.TURNOUT:
                        drawable = ((Turnout) blockBase).getTurnoutDrawable()[((Turnout) blockBase).getOrientation()];
                        break;
                    case ConstantValue.USABLE:
                        continue;
                }
                Bitmap bitmap = Drawable2Bitmap.drawableToBitmap(drawable);
                mPaint.setColor(blockBase.getColor());
                canvas.drawBitmap(bitmap, 0, 0, mPaint);
            }
        }
    }

    public void setBlockBase(int layer, BlockBase blockBase) {
        mBlockBaseHashMap.remove(layer);
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
