package com.sorashiro.metroplanning.util;

import android.app.Activity;

import java.util.Stack;

public class ActivityManager {

    private static Stack           activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getScreenManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        if (activityStack == null) {
            activityStack = new Stack();
        }
        return instance;
    }

    public void popActivity() {
        Activity activity = (Activity) activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public Activity currentActivity() {
        if (activityStack.empty()) {
            return null;
        }
        Activity activity = (Activity) activityStack.lastElement();
        return activity;
    }

    public Class<?> currentActivityCls() {
        if (activityStack.empty()) {
            return null;
        }
        Activity activity = (Activity) activityStack.lastElement();
        return activity.getClass();
    }

    public void pushActivity(Activity activity) {

        activityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void popAll() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
} 