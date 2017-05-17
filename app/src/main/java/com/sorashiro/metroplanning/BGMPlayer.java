package com.sorashiro.metroplanning;

import android.content.Context;
import android.media.MediaPlayer;

public class BGMPlayer {

    private static BGMPlayer instance = null;

    private static MediaPlayer mMediaPlayer;

    private BGMPlayer() {

    }

    public static BGMPlayer getInstance(Context context) {
        synchronized (BGMPlayer.class) {
            if (instance == null) {
                mMediaPlayer = new MediaPlayer();
                Context app = context.getApplicationContext();
                mMediaPlayer = MediaPlayer.create(app, ConstantValue.GAME_MUSIC);
                mMediaPlayer.setLooping(true);
                instance = new BGMPlayer();
            }
        }
        return instance;
    }

    public static MediaPlayer getMediaPlayer(Context context) {
        if(mMediaPlayer == null) {
            getInstance(context);
        }
        return mMediaPlayer;
    }
}
