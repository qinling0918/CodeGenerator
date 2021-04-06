package me.tsinling.code.generator.utils;

import android.content.Context;

import java.util.ArrayList;


/**
 * created by tsinling on: 2018/4/29 08:42
 * description:
 */
public class PrefUtils {
    public static final String SP_NAME_HISTORY = "history";

    public static final String FACTORY_NAME = "factory_name";
    public static final String FACTORY_CODE = "factory_code";

    public static PrefHelper getSp() {
        return PrefHelper.getInstance("");
    }

    public static PrefHelper getSp(String spName) {
        return PrefHelper.getInstance(spName);
    }

    public static PrefHelper history() {
        return getSp(SP_NAME_HISTORY);
    }

    public static void setFactoryNameHistory(ArrayList<String> factory_name) {
        saveHistories( FACTORY_NAME, factory_name);
    }

    public static ArrayList<String> getFactoryNameHistory() {
        return getHistories(FACTORY_NAME);
    }

    public static void setFactoryCodeHistory(ArrayList<String> factory_code) {
        saveHistories( FACTORY_CODE, factory_code);
    }

    public static ArrayList<String> getFactoryCodeHistory() {
        return getHistories(FACTORY_CODE);
    }


    public static Context getContext() {
        return Utils.getContext();
    }

    public static ArrayList<String> getHistories(String key) {
        return PrefHelper.getInstance(SP_NAME_HISTORY).getSerializable(key, new ArrayList<String>());
    }

    public static void saveHistories(String key, ArrayList<String> history) {
        PrefHelper.getInstance(SP_NAME_HISTORY).edit().put(key, history).commit();
    }

    public static void clearHistories(String key) {
        saveHistories(key,new ArrayList<String>());
    }




}
