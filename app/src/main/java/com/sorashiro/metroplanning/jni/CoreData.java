package com.sorashiro.metroplanning.jni;

public class CoreData {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    public static native String getLevelData(int level);

    public static native int getLevels();
}
