package com.sorashiro.metroplanning;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.sorashiro.metroplanning.adapter.LevelListAdapter;
import com.sorashiro.metroplanning.jni.CoreData;
import com.sorashiro.metroplanning.util.AppSaveDataSPUtil;
import com.sorashiro.metroplanning.util.LogAndToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GameKing on 2017/5/5.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R2.id.level_list)
    RecyclerView mRcLevelList;

    private List<String> mListData;
    private LevelListAdapter mLevelListAdapter;


    private MediaPlayer   mMediaPlayer;

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
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying() && AppSaveDataSPUtil.getIfMusicOn()) {
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
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
        //初始化BGM参数
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, ConstantValue.GAME_MUSIC);
        mMediaPlayer.setLooping(true);
    }

    private void initData() {
        mListData = new ArrayList<>();
        int levels = CoreData.getLevels();
        for(int i = 1; i <= levels; i++) {
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
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("level", position+1);
                startActivity(intent);
            }
        });
        mRcLevelList.setAdapter(mLevelListAdapter);
    }

}
