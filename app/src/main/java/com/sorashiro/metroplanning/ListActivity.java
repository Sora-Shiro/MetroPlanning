package com.sorashiro.metroplanning;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sorashiro.metroplanning.util.AppSaveDataSPUtil;
import com.sorashiro.metroplanning.util.AppUtil;
import com.sorashiro.metroplanning.util.LogAndToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GameKing on 2017/5/6.
 */

public class ListActivity extends AppCompatActivity {

    @BindView(R2.id.text_version)
    TextView mTextVersion;
    @BindView(R2.id.text_title)
    TextView mTextTitle;
    @BindView(R2.id.text_work)
    TextView mTextWork;

    MediaPlayer mMediaPlayer;

    private int lanType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_list);

        AppSaveDataSPUtil.init(this);

        lanType = AppSaveDataSPUtil.getLanguage();

        initMusic();

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTextTitle.setText(GetResourceUtil.getString(this, "metro_n_planning", lanType));
        mTextWork.setText(GetResourceUtil.getString(this, "program_music", lanType));

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
        try {
            String text = "v" + AppUtil.getVersionName(this);
            mTextVersion.setText(text);
        } catch (Exception e) {
            LogAndToastUtil.LogV(e.getMessage());
            e.printStackTrace();
        }
    }

    @OnClick(R.id.layout_root)
    public void onClick(View view) {
        finish();
    }

}
