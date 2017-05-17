package com.sorashiro.metroplanning;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class GetResourceUtil {

    public static String getString(Context context, String name, int lanType) {

        String suffix = "";

        switch (lanType) {
            case ConstantValue.ENGLISH:
                break;
            case ConstantValue.CHINESE_SIM:
                suffix = "_chn";
                break;
        }

        int id = context.getResources().getIdentifier(
                name+suffix, "string", context.getPackageName());

        return context.getString(id);
    }

    public static Drawable getDrawable(Context context, String name, int lanType) {
        String suffix = "";

        switch (lanType) {
            case ConstantValue.ENGLISH:
                break;
            case ConstantValue.CHINESE_SIM:
                suffix = "_chn";
                break;
        }

        int id = context.getResources().getIdentifier(
                name+suffix, "drawable", context.getPackageName());

        return context.getResources().getDrawable(id);
    }

}
