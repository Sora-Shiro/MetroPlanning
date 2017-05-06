package com.sorashiro.metroplanning;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.sorashiro.metroplanning.util.AnimationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExitDialog extends Dialog {

    @BindView(R.id.img_yes)
    ImageView mImgYes;
    @BindView(R.id.img_no)
    ImageView mImgNo;

    private ExitEvent mExitEvent;
    private Context   mContext;

    interface ExitEvent {
        void exit();
        void noExit();
    }

    public ExitDialog(Context context) {
        super(context, R.style.add_dialog);
        this.mContext = context;
    }

    public ExitDialog(Context context, int themeResId) {
        super(context, R.style.add_dialog);
    }

    public ExitDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit);
        ButterKnife.bind(this);
    }

    public void setExitEvent(ExitEvent p){
        this.mExitEvent = p;
    }

    @OnClick({R.id.img_yes, R.id.img_no})
    public void onClick(View view){
        AnimationUtil.twinkle(view);
        switch (view.getId()){
            case R.id.img_yes:
                mExitEvent.exit();
                break;
            case R.id.img_no:
                mExitEvent.noExit();
                break;
        }
        dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}