package com.sorashiro.metroplanning.jni;

/**
 * Created by GameKing on 2017/5/4.
 */

public class CoreData {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    public static native String getLevelData(int level);

    public static native int getLevels();
}
