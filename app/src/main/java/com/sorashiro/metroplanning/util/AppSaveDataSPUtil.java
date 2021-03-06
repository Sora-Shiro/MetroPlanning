package com.sorashiro.metroplanning.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sorashiro.metroplanning.ConstantValue;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * @author Sora
 * @date 2016/11/7
 * <p>
 * A class to use SharedPreferences conveniently.
 * MUST call init() BEFORE call other methods!
 * 一个方便使用SharedPreferences的类。
 * 调用其他方法前，请务必先调用init()方法！
 */

public class AppSaveDataSPUtil {

    private static WeakReference<Context> sContext;

    //Main Catalog
    //主目录
    private static final String DATA_CONFIG = "data_config";

    private static final String IF_MUSIC_ON = "if_music_on";

    private static final String PASS_LEVEL = "pass_level";

    private static final String LANGUAGE = "language";

    private static SharedPreferences        sSharedPreferences;
    private static SharedPreferences.Editor sEditor;

    private AppSaveDataSPUtil() {
    }

    //Must call this function before change or get the data
    //修改或获得数据前必须先调用该函数
    public static void init(Context context) {
        if (sContext != null) {
            sContext = null;
        }
        sContext = new WeakReference<>(context);
        sSharedPreferences = context.getSharedPreferences(DATA_CONFIG, Context.MODE_PRIVATE);
        sEditor = sSharedPreferences.edit();
        sEditor.commit();
    }

    public static void setIfMusicOn(boolean set) {
        sEditor.putBoolean(IF_MUSIC_ON, set);
        sEditor.commit();
    }

    public static boolean getIfMusicOn() {
        return sSharedPreferences.getBoolean(IF_MUSIC_ON, true);
    }

    public static void setPassLevel(int passLevel) {
        sEditor.putInt(PASS_LEVEL, passLevel);
        sEditor.commit();
    }

    public static int getPassLevel(){
        return sSharedPreferences.getInt(PASS_LEVEL, 0);
    }

    public static void setLanguage(int language) {
        sEditor.putInt(LANGUAGE, language);
        sEditor.commit();
    }

    public static int getLanguage() {
        return sSharedPreferences.getInt(LANGUAGE, defaultLanguage(sContext.get()));
    }

    private static int defaultLanguage(Context context) {
        if(isZh(context)) {
            return ConstantValue.CHINESE_SIM;
        } else {
            return ConstantValue.ENGLISH;
        }
    }

    private static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }

}
