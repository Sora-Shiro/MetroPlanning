package com.sorashiro.metroplanning;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorashiro.metroplanning.util.AppSaveDataSPUtil;
import com.sorashiro.metroplanning.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfigActivity extends AppCompatActivity {

    @BindView(R2.id.text_lan)
    TextView mTextLan;
    @BindView(R2.id.img_music)
    ImageView mImgMusic;

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        AppSaveDataSPUtil.init(this);

        initMusic();

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying() && AppSaveDataSPUtil.getIfMusicOn()) {
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && !AppUtil.isAppOnForeground(this)) {
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //防止后台进入刷新，对部分机型无效
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initMusic() {
        mMediaPlayer = BGMPlayer.getMediaPlayer(this);
    }

    private void initView() {
        ButterKnife.bind(this);

        if(AppSaveDataSPUtil.getIfMusicOn()){
            mImgMusic.setImageDrawable(getResources().getDrawable(R.drawable.audiotrack_dark));
        } else {
            mImgMusic.setImageDrawable(getResources().getDrawable(R.drawable.audiotrack_white));
        }

        switch (AppSaveDataSPUtil.getLanguage()) {
            case ConstantValue.ENGLISH:
                mTextLan.setText(getText(R.string.eng));
                break;
            case ConstantValue.CHINESE_SIM:
                mTextLan.setText(getText(R.string.chis));
                break;
        }

    }

    @OnClick({R.id.img_music, R.id.text_lan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_music:
                toggleMediaPlayer();
                break;
            case R.id.text_lan:
                toggleLanguage();
                break;
        }
    }

    //更改BGM状态时要调用该函数
    private void toggleMediaPlayer() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            AppSaveDataSPUtil.setIfMusicOn(false);
            mImgMusic.setImageDrawable(getResources().getDrawable(R.drawable.audiotrack_white));
        } else {
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
                AppSaveDataSPUtil.setIfMusicOn(true);
                mImgMusic.setImageDrawable(getResources().getDrawable(R.drawable.audiotrack_dark));
            }
        }
    }

    private void toggleLanguage() {
        int lan = (AppSaveDataSPUtil.getLanguage()+1) % ConstantValue.LAN_SUM;
        switch (lan) {
            case ConstantValue.ENGLISH:
                mTextLan.setText(getText(R.string.eng));
                AppSaveDataSPUtil.setLanguage(ConstantValue.ENGLISH);
                break;
            case ConstantValue.CHINESE_SIM:
                mTextLan.setText(getText(R.string.chis));
                AppSaveDataSPUtil.setLanguage(ConstantValue.CHINESE_SIM);
                break;
        }
    }


}
