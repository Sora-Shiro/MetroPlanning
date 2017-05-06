package com.sorashiro.metroplanning;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sorashiro.metroplanning.util.AppSaveDataSPUtil;
import com.sorashiro.metroplanning.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GameKing on 2017/5/6.
 */

public class HelpActivity extends AppCompatActivity {

    MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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
    }

    @OnClick(R.id.layout_root)
    public void onClick(View view) {
        finish();
    }

}
