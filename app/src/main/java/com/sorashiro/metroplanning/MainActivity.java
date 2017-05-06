package com.sorashiro.metroplanning;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.sorashiro.metroplanning.adapter.LevelListAdapter;
import com.sorashiro.metroplanning.jni.CoreData;
import com.sorashiro.metroplanning.util.AnimationUtil;
import com.sorashiro.metroplanning.util.AppSaveDataSPUtil;
import com.sorashiro.metroplanning.util.AppUtil;
import com.sorashiro.metroplanning.util.LogAndToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GameKing on 2017/5/5.
 */

public class MainActivity extends AppCompatActivity implements ExitDialog.ExitEvent {

    @BindView(R2.id.level_list)
    RecyclerView mRcLevelList;

    private List<String> mListData;
    private LevelListAdapter mLevelListAdapter;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppSaveDataSPUtil.init(this);

        initMusic();

        initData();

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        int level = AppSaveDataSPUtil.getPassLevel() + 1;
        if (level <= CoreData.getLevels() && mListData.size() < level) {
            mListData.add(mListData.size(), level+"");
            mLevelListAdapter.notifyItemInserted(mListData.size());
            mRcLevelList.scrollToPosition(mListData.size());
        }
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

    private void initData() {
        mListData = new ArrayList<>();
        int levels = CoreData.getLevels();
        int next = AppSaveDataSPUtil.getPassLevel() + 1;
        int less = levels < next ? levels : next;
        for (int i = 1; i <= less; i++) {
            mListData.add(i+"");
        }
    }

    private void initView() {
        ButterKnife.bind(this);

        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        mRcLevelList.setLayoutManager(layoutManager);
        mRcLevelList.setHasFixedSize(true);
        mRcLevelList.addOnScrollListener(new CenterScrollListener());

        mRcLevelList.setItemAnimator(new DefaultItemAnimator());

        mLevelListAdapter = new LevelListAdapter(this, mListData);
        mLevelListAdapter.setOnItemClickListener(new LevelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LogAndToastUtil.ToastOut(MainActivity.this, "Long Touch To Play :)");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                LogAndToastUtil.ToastOut(MainActivity.this, "Loading...");
                if(position == 0) {
                    Intent intent = new Intent(MainActivity.this, GameTutorialActivity.class);
                    intent.putExtra("level", position+1);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("level", position+1);
                    startActivity(intent);
                }

            }
        });
        mRcLevelList.setAdapter(mLevelListAdapter);
        mRcLevelList.scrollToPosition(mListData.size());
    }

    @OnClick({R.id.btn_setting, R.id.btn_help, R.id.btn_list, R.id.btn_exit})
    public void onBtnClick(View view) {
        AnimationUtil.twinkle(view);
        switch (view.getId()) {
            case R.id.btn_setting:
                break;
            case R.id.btn_help:
                Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.btn_list:
                Intent listIntent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(listIntent);
                break;
            case R.id.btn_exit:
                showExitDialog();
                break;
        }
    }

    private void showExitDialog() {
        ExitDialog exitDialog = new ExitDialog(this);
        Window window = exitDialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.CENTER);
        exitDialog.setExitEvent(this);
        exitDialog.show();
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void noExit() {
        //Do Nothing
    }

    //更改BGM状态时要调用该函数
    private void toggleMediaPlayer() {
        if(!AppSaveDataSPUtil.getIfMusicOn()){
            return;
        }
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

}
