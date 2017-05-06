package com.sorashiro.metroplanning;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.sorashiro.metroplanning.jni.CoreData;
import com.sorashiro.metroplanning.model.BlockBase;
import com.sorashiro.metroplanning.model.Metro;
import com.sorashiro.metroplanning.model.Station;
import com.sorashiro.metroplanning.model.Turnout;
import com.sorashiro.metroplanning.model.Usable;
import com.sorashiro.metroplanning.util.AnimationUtil;
import com.sorashiro.metroplanning.util.AppSaveDataSPUtil;
import com.sorashiro.metroplanning.util.LogAndToastUtil;
import com.sorashiro.metroplanning.util.ShapeUtil;
import com.sorashiro.metroplanning.view.BlockView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R2.id.progress_time)
    IconRoundCornerProgressBar mProgressTime;
    @BindView(R2.id.progress_passenger)
    IconRoundCornerProgressBar mProgressPassenger;
    @BindView(R2.id.btn_start)
    Button                     mBtnStart;
    @BindView(R2.id.text_game_over)
    TextView                   mTextGameOver;

    private volatile boolean ifGameOver = false;

    private MediaPlayer mMediaPlayer;

    private int mLevel = 1;
    private int mMapSize   = ConstantValue.MAP_SIZE;
    private int mLayerSize = ConstantValue.LAYER_SIZE;
    private BlockBase[][][] mMapTotal;
    private BlockBase[][][] mMapCurrent;
    private BlockView[][][] mMapImg;

    private ArrayList<Metro> mMetros;

    private int targetTime;
    private int remainTime;
    private int targetPassenger;
    private int finishPassenger;

    private ShapeDrawable mNothingDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        AppSaveDataSPUtil.init(this);

        initMusic();

        initMap();

        initTarget();

        initView();

        initRxJava();

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
        ifGamePause = true;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    //防止后台进入刷新，对部分机型无效
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    boolean firstFocus = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && firstFocus) {
            firstFocus = false;

            int pth = mProgressTime.getHeight() - mProgressTime.getIconPadding();
            mProgressTime.setIconSize(pth);
            mProgressPassenger.setIconSize(pth);

            canStartGameAnimation();
        }
    }
    private void canStartGameAnimation() {
        String text  = "Level\n" + mLevel;
        mTextGameOver.setText(text);
        YoYo.with(Techniques.RollIn).duration(1000).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                YoYo.with(Techniques.RollOut).delay(1000).duration(1000).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        YoYo.with(Techniques.Flash).duration(1000).onStart(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                mTextGameOver.setVisibility(View.GONE);
                                mBtnStart.setClickable(true);
                            }
                        }).playOn(mBtnStart);
                    }
                }).playOn(mTextGameOver);
            }
        }).playOn(mTextGameOver);
    }

    private void initMusic() {
        //初始化BGM参数
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, ConstantValue.GAME_MUSIC);
        mMediaPlayer.setLooping(true);
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

    private void initMap() {
        mLevel = getIntent().getIntExtra("level", 1);
        String gameData = CoreData.getLevelData(mLevel);
        String[] gameDataSegments = gameData.split(",");
        String[] mapData = gameDataSegments[0].split(" ");
        String[] metroData = gameDataSegments[1].split(" ");
        String[] stationData = gameDataSegments[2].split(" ");
        String[] turnoutData = gameDataSegments[3].split(" ");
        //地图总宽高，暂时用不到
        int mapWidth = Integer.parseInt(mapData[0]);
        int mapHeight = Integer.parseInt(mapData[1]);
        targetTime = Integer.parseInt(mapData[2]);
        targetPassenger = Integer.parseInt(mapData[3]);

        //初始化Model层
        mMapCurrent = new BlockBase[mLayerSize][mMapSize][mMapSize];
        int colorUniversal = getResources().getColor(R.color.block_universal);
        for (int i = 0; i < mLayerSize; i++) {
            for (int j = 0; j < mMapSize; j++) {
                for (int k = 0; k < mMapSize; k++) {
                    mMapCurrent[i][j][k] = new Usable(i, j, colorUniversal);
                }
            }
        }

        //初始化颜色
        int black = getResources().getColor(R.color.block_black);
        int blackTr = getResources().getColor(R.color.block_black_transparent);

        HashMap<String, Integer> oriMap = new HashMap<>();
        oriMap.put("down", ConstantValue.DOWN);
        oriMap.put("up", ConstantValue.UP);
        oriMap.put("left", ConstantValue.LEFT);
        oriMap.put("right", ConstantValue.RIGHT);

        SparseArray<ShapeDrawable[]> metroDrawables = new SparseArray<>();
        mMetros = new ArrayList<>();
        //i从1开始，不读取Block总数量
        for (int i = 1; i < metroData.length; i += 6) {
            int x = Integer.parseInt(metroData[i]);
            int y = Integer.parseInt(metroData[i + 1]);
            //注意地铁在第二层View中，颜色要比第一层略浅，但是color属性还是black，方便识别
            int fadeColor = getResources().getColor(getResources().getIdentifier(
                    "block_" + metroData[i + 2], "color", getPackageName()));
            int color = getResources().getColor(getResources().getIdentifier(
                    "block_" + metroData[i + 2] + "_transparent", "color", getPackageName()));
            int orientation = oriMap.get(metroData[i + 3]);
            int speed = Integer.parseInt(metroData[i + 4]);
            int fullLoad = Integer.parseInt(metroData[i + 5]);
            ShapeDrawable[] drawables;
            if (metroDrawables.get(color) != null) {
                drawables = metroDrawables.get(color);
            } else {
                drawables = ShapeUtil.getRegTris(color);
                metroDrawables.put(color, drawables);
            }
            Metro metro = new Metro(
                    x, y, fadeColor, orientation,
                    speed, fullLoad, drawables
            );
            mMapCurrent[1][x][y] = metro;
            mMetros.add(metro);
        }

        SparseArray<ShapeDrawable> stationDrawables = new SparseArray<>();
        for (int i = 1; i < stationData.length; i += 4) {
            int x = Integer.parseInt(stationData[i]);
            int y = Integer.parseInt(stationData[i + 1]);
            int color = getResources().getColor(getResources().getIdentifier(
                    "block_" + stationData[i + 2], "color", getPackageName()));
            int passenger = Integer.parseInt(stationData[i + 3]);
            ShapeDrawable drawables;
            if (stationDrawables.get(color) != null) {
                drawables = stationDrawables.get(color);
            } else {
                drawables = ShapeUtil.getRectangleDrawable(color);
                stationDrawables.put(color, drawables);
            }
            Station station = new Station(
                    x, y, color,
                    passenger, drawables);
            mMapCurrent[0][x][y] = station;
        }

        SparseArray<ShapeDrawable[]> turnoutDrawables = new SparseArray<>();
        for (int i = 1; i < turnoutData.length; i += 4) {
            int x = Integer.parseInt(turnoutData[i]);
            int y = Integer.parseInt(turnoutData[i + 1]);
            int color = getResources().getColor(getResources().getIdentifier(
                    "block_" + turnoutData[i + 2], "color", getPackageName()));
            int orientation = oriMap.get(turnoutData[i + 3]);
            ShapeDrawable[] drawables;
            if (turnoutDrawables.get(color) != null) {
                drawables = turnoutDrawables.get(color);
            } else {
                drawables = ShapeUtil.getArrows(color);
                turnoutDrawables.put(color, drawables);
            }
            Turnout turnout = new Turnout(x, y, color, orientation, drawables);
            mMapCurrent[0][x][y] = turnout;
        }

    }

    private void initTarget() {
        remainTime = 0;
        finishPassenger = 0;
    }

    private void initView() {
        ButterKnife.bind(this);

        mBtnStart.setClickable(false);

        mProgressTime.setMax(targetTime);
        mProgressTime.setProgress(remainTime);
        mProgressPassenger.setMax(targetPassenger);
        mProgressPassenger.setProgress(finishPassenger);

        mNothingDrawable = ShapeUtil.getNothingDrawable();
        mMapImg = new BlockView[mLayerSize][mMapSize][mMapSize];
        for (int i = 0; i < mLayerSize; i++) {
            for (int j = 0; j < mMapSize; j++) {
                for (int k = 0; k < mMapSize; k++) {
                    mMapImg[i][j][k] = (BlockView) findViewById(
                            getResources().getIdentifier(
                                    "map_" + i + "_" + j + "_" + k, "id", getPackageName()));
                    mMapImg[i][j][k].setOnClickListener(this);
                    mMapImg[i][j][k].setBlockX(j);
                    mMapImg[i][j][k].setBlockY(k);
                    mMapImg[i][j][k].setBlockZ(i);
                    switch (mMapCurrent[i][j][k].getType()) {
                        case ConstantValue.METRO:
                            Metro metro = (Metro) mMapCurrent[i][j][k];
                            mMapImg[i][j][k].setBackgroundDrawable(
                                    metro.getMetroDrawable()[metro.getOrientation()]);
                            String metroText = metro.getPassenger() + "";
                            mMapImg[i][j][k].setText(metroText);
                            mMapImg[i][j][k].setVisibility(View.VISIBLE);
                            break;
                        case ConstantValue.STATION:
                            Station station = (Station) mMapCurrent[i][j][k];
                            mMapImg[i][j][k].setBackgroundDrawable(
                                    station.getStationDrawable()
                            );
                            int passenger = station.getPassenger();
                            int priority = station.getPriority();
                            String text = passenger + "\n" + priority;
                            mMapImg[i][j][k].setText(text);
                            mMapImg[i][j][k].setVisibility(View.VISIBLE);
                            break;
                        case ConstantValue.TURNOUT:
                            Turnout turnout = (Turnout) mMapCurrent[i][j][k];
                            mMapImg[i][j][k].setBackgroundDrawable(
                                    turnout.getTurnoutDrawable()[turnout.getOrientation()]
                            );
                            mMapImg[i][j][k].setVisibility(View.VISIBLE);
                            break;
                        case ConstantValue.OUTBOUND:
                            break;
                        case ConstantValue.USABLE:
                            mMapImg[i][j][k].setBackgroundDrawable(mNothingDrawable);
                            mMapImg[i][j][k].setVisibility(View.GONE);
                            break;
                    }
                }
            }
        }


    }


    private BlockBase getBlock(int x, int y) {
        if (x < 0 || y < 0 || x >= mMapSize || y >= mMapSize) {
            return null;
        }
        return mMapCurrent[0][x][y];
    }

    //涉及到大量的计算，应该把处理UI的部分独立出来
    public void updateMetroPosition(int beforeX, int beforeY, int afterX, int afterY) {
        if (ifGameOver) {
            return;
        }
        moveMetroToPosition(beforeX, beforeY, afterX, afterY);
        updateMetroOrientation(afterX, afterY);
        updateMetroPassenger(afterX, afterY);
    }

    private boolean checkPositionState(int bx, int by, int x, int y) {
        if (x < 0 || y < 0 || x >= mMapSize || y >= mMapSize) {
            failedGame(bx, by, x, y, ConstantValue.FAIL_OUT_BOUND);
            return false;
        }
        for (int z = 0; z < mLayerSize; z++) {
            int type = mMapCurrent[z][x][y].getType();
            switch (type) {
                case ConstantValue.METRO:
                    failedGame(bx, by, x, y, ConstantValue.FAIL_HIT_SOMETHING);
                    return false;
                case ConstantValue.STATION:
                    failedGame(bx, by, x, y, ConstantValue.FAIL_HIT_SOMETHING);
                    return false;
                case ConstantValue.TURNOUT:
                    break;
                case ConstantValue.OUTBOUND:
                    break;
                case ConstantValue.USABLE:
                    break;
            }
        }
        return true;
    }

    private void moveMetroToPosition(final int beforeX, final int beforeY, final int afterX, final int afterY) {
        final Metro metro = (Metro) (mMapCurrent[1][beforeX][beforeY]);
        mMapCurrent[1][beforeX][beforeY] = new Usable(
                beforeX, beforeY, getResources().getColor(R.color.block_universal));
        mMapCurrent[1][afterX][afterY] = metro;

        final ShapeDrawable drawable = (ShapeDrawable) metro.getMetroDrawable()[metro.getOrientation()];
        final String text = metro.getPassenger() + "";
        //主线程操作
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMapImg[1][beforeX][beforeY].setBackgroundDrawable(mNothingDrawable);
                mMapImg[1][beforeX][beforeY].setText("");
                mMapImg[1][beforeX][beforeY].setVisibility(View.GONE);
                mMapImg[1][afterX][afterY].setBackgroundDrawable(drawable);
                mMapImg[1][afterX][afterY].setText(text);
                mMapImg[1][afterX][afterY].setVisibility(View.VISIBLE);
            }
        });

    }

    private void updateMetroOrientation(final int x, final int y) {
        if (mMapCurrent[0][x][y].getType() == ConstantValue.TURNOUT) {
            Turnout turnout = (Turnout) mMapCurrent[0][x][y];
            final int orientation = turnout.getOrientation();
            final Metro metro = (Metro) mMapCurrent[1][x][y];
            final TextView textView = mMapImg[1][x][y];
            //主线程操作
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    metro.setOrientation(orientation);
                    textView.setBackgroundDrawable(metro.getMetroDrawable()[orientation]);
                }
            });
        }
    }

    private void updateMetroPassenger(final int x, final int y) {
        ArrayList<BlockBase> list = new ArrayList<>();
        BlockBase up = getBlock(x - 1, y);
        BlockBase down = getBlock(x + 1, y);
        BlockBase left = getBlock(x, y - 1);
        BlockBase right = getBlock(x, y + 1);
        //你是不是地铁站啊？
        if (up != null && up instanceof Station) {
            list.add(up);
        }
        if (down != null && down instanceof Station) {
            list.add(down);
        }
        if (left != null && left instanceof Station) {
            list.add(left);
        }
        if (right != null && right instanceof Station) {
            list.add(right);
        }
        int index = -1, max = -1;
        for (int i = 0; i < list.size(); i++) {
            Station station = (Station) list.get(i);
            if (max < station.getPriority()) {
                max = station.getPriority();
                index = i;
            }
        }
        //没有地铁站！
        if (index == -1) {
            return;
        }
        //地铁站优先级一样！不运输了！
        for (int i = 0; i < list.size(); i++) {
            Station station = (Station) list.get(i);
            if (max == station.getPriority() && i != index) {
                return;
            }
        }

        //回到了出发地！
        final Metro metro = (Metro) mMapCurrent[1][x][y];
        int preStationX = metro.getPreStationX();
        int preStationY = metro.getPreStationY();
        Station station = (Station) list.get(index);
        final int curStationX = station.getX();
        final int curStationY = station.getY();
        if (preStationX == curStationX && preStationY == curStationY) {
            return;
        }

        //地铁和地铁站的颜色不是通用颜色且不一样！
        int metroColor = metro.getColor();
        int stationColor = station.getColor();
        int colorUniversal = getResources().getColor(R.color.block_universal);
        if (metroColor != colorUniversal && stationColor != colorUniversal
                && metroColor != stationColor) {
            return;
        }

        int metroPassenger = metro.getPassenger();
        int stationPassenger = station.getPassenger();

        //处理乘客需要时间重新出发
        if (metroPassenger != 0 || stationPassenger != 0) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    metro.setTempDelay(1000);
                }
            });
        }

        //地铁有乘客
        if (metroPassenger != 0) {
            finishPassenger += metroPassenger;
            //主线程操作
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mMapImg[1][x][y].setText("0");
                    metro.setPassenger(0);
                    mProgressPassenger.setProgress(finishPassenger);
                    //运送了要记住出发点
                    metro.setPreStationX(curStationX);
                    metro.setPreStationY(curStationY);
                }
            });
            //检查运输进度
            if (checkTransportProcess()) {
                finishGame();
                return;
            }
        }

        //地铁站没人！
        if (stationPassenger == 0) {
            return;
        }

        //运送乘客
        int fullLoad = metro.getFullLoad();
        final int loadPassenger = fullLoad < stationPassenger ? fullLoad : stationPassenger;

        //主线程操作
        final String metroText = loadPassenger + "";
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mMapImg[1][x][y].setText(metroText);
                metro.setPassenger(loadPassenger);
                //运送了要记住出发点
                metro.setPreStationX(curStationX);
                metro.setPreStationY(curStationY);
            }
        });
        //地铁站乘客数量处理
        stationPassenger -= loadPassenger;
        station.setPassenger(stationPassenger);
        int priority = station.getPriority();
        final String stationText = stationPassenger + "\n" + priority;
        final TextView stationTv = mMapImg[0][curStationX][curStationY];
        //主线程操作
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                stationTv.setText(stationText);
            }
        });
    }

    private boolean checkTransportProcess() {
        return finishPassenger >= targetPassenger;
    }

    private void finishGame() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ifGameOver = true;
                mBtnStart.setClickable(false);
                mTextGameOver.setText(getString(R.string.finish_level));
                gameOverAnimation();
            }
        });
    }

    private void failedGame(final int bx, final int by, final int x, final int y, final int failType) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ifGameOver = true;
                mBtnStart.setClickable(false);
                if (failType == ConstantValue.FAIL_TIME_OUT) {

                    mTextGameOver.setText(getString(R.string.time_out));
                    gameOverAnimation();
                } else if (failType == ConstantValue.FAIL_OUT_BOUND) {
                    mMapImg[1][bx][by].setVisibility(View.GONE);
                    mTextGameOver.setText(getString(R.string.game_over));
                    gameOverAnimation();
                } else if (failType == ConstantValue.FAIL_HIT_SOMETHING) {
                    Drawable brust = getResources().getDrawable(R.drawable.burst);
                    mMapImg[1][bx][by].setBackgroundDrawable(mNothingDrawable);
                    mMapImg[0][bx][by].setText("");
                    mMapImg[1][bx][by].setText("");
                    mMapImg[0][x][y].setText("");
                    mMapImg[1][x][y].setText("");
                    mMapImg[0][x][y].setVisibility(View.GONE);
                    mMapImg[1][x][y].setBackgroundDrawable(brust);
                    mMapImg[1][x][y].setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.Wobble).duration(500).onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            YoYo.with(Techniques.TakingOff).duration(500).onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    mTextGameOver.setText(getString(R.string.game_over));
                                    gameOverAnimation();
                                }
                            }).playOn(mMapImg[1][x][y]);
                        }
                    }).playOn(mMapImg[1][x][y]);
                }
            }
        });
    }

    private void gameOverAnimation() {
        mTextGameOver.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceInDown).duration(1000).repeat(1).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                YoYo.with(Techniques.TakingOff).delay(1000).duration(1000).repeat(1).onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        YoYo.with(Techniques.RubberBand).duration(1000).repeat(1).onStart(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                mBtnStart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                                mBtnStart.setText(R.string.ok);
                                mBtnStart.setClickable(true);
                            }
                        }).playOn(mBtnStart);
                    }
                }).playOn(mTextGameOver);
            }
        }).playOn(mTextGameOver);
    }

    boolean ifGameStart = false;
    boolean ifGamePause = false;

    @OnClick(R.id.btn_start)
    public void onBtnStartClick(View view) {
        if (ifGameStart) {
            ifGamePause = !ifGamePause;
            changeBtnText();
            return;
        }
        ifGameStart = true;
        changeBtnText();

        //倒计时启动
        Flowable<String> timer = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                if (!ifGamePause) {
                    Thread.sleep(1000);
                    e.onNext("");
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER).repeat();
        timer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timeConsumer);

        //地铁启动
        ArrayList<Flowable<String>> flowables = new ArrayList<>();
        for (final Metro metro : mMetros) {
            Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
                @Override
                public void subscribe(FlowableEmitter<String> e) throws Exception {
                    if (!ifGamePause && metro.isDriving() && !ifGameOver) {
                        Thread.sleep(metro.getSpeed() + metro.getTempDelay());
                        if (!ifGamePause && metro.isDriving() && !ifGameOver) {
                            metro.setTempDelay(0);
                            metro.updatePosition();
                            if (!checkPositionState(
                                    metro.getX(),
                                    metro.getY(),
                                    metro.getChangeX(),
                                    metro.getChangeY())) {
                                return;
                            }
                            updateMetroPosition(
                                    metro.getX(),
                                    metro.getY(),
                                    metro.getChangeX(),
                                    metro.getChangeY());
                            metro.setX(metro.getChangeX());
                            metro.setY(metro.getChangeY());
                        }
                    }
                    e.onComplete();
                }
            }, BackpressureStrategy.BUFFER).repeat();
            flowables.add(flowable);
        }
        for (Flowable<String> flowable : flowables) {
            flowable.subscribeOn(Schedulers.computation())
                    .subscribe(metroConsumer);
        }

    }

    private void changeBtnText() {
        if (!ifGamePause) {
            mBtnStart.setText(getString(R.string.pause));
        } else {
            mBtnStart.setText(getString(R.string.resume));
        }
    }

    Consumer<String> metroConsumer;
    Consumer<String> timeConsumer;

    private void initRxJava() {
        mHandler = new UIHandler(this);

        timeConsumer = new Consumer<String>() {
            @Override
            public void accept(String integer) throws Exception {
                if (!ifGamePause && !ifGameOver) {
                    mProgressTime.setProgress(remainTime++);
                    if (remainTime >= targetTime) {
                        ifGameOver = true;
                        failedGame(-1, -1, -1, -1, ConstantValue.FAIL_TIME_OUT);
                    }
                }
            }
        };
        metroConsumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //Nothing To Do
            }
        };
    }

    private Handler mHandler;

    private static class UIHandler extends Handler {

        private final WeakReference<AppCompatActivity> mActivity;

        private UIHandler(AppCompatActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    }

    @Override
    public void onClick(View v) {
        BlockView blockView = (BlockView) v;
        int x = blockView.getBlockX();
        int y = blockView.getBlockY();
        int z = blockView.getBlockZ();
        int type = mMapCurrent[z][x][y].getType();
        AnimationUtil.twinkle(v);
        switch (z) {
            case 0:
                if (type == ConstantValue.TURNOUT) {
                    Turnout turnout = ((Turnout) mMapCurrent[z][x][y]);
                    turnout.rotateClockwise();
                    mMapImg[z][x][y].setBackgroundDrawable(
                            turnout.getTurnoutDrawable()[turnout.getOrientation()]);
                } else if (type == ConstantValue.STATION) {
                    Station station = ((Station) mMapCurrent[z][x][y]);
                    int priority = (station.getPriority() + 1) % 5;
                    priority = priority == 0 ? 5 : priority;
                    station.setPriority(priority);
                    mMapImg[z][x][y].setText(station.getPassenger() + "\n" + station.getPriority());
                }
                break;
            case 1:
                if (type == ConstantValue.METRO) {
                    ((Metro) mMapCurrent[z][x][y])
                            .setDriving(
                                    !((Metro) mMapCurrent[z][x][y]).isDriving());
                }
                break;
        }
    }

    @OnClick(R.id.text_game_over)
    public void onTextGameOverClick() {
        //Do Nothing
    }

}
