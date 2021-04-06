package me.tsinling.code.generator.utils;

import android.app.Application;
import android.content.Context;

import java.util.Locale;

/**
 * Created by qinling on 2018/9/17 10:03
 * Description:
 */
public class Utils {
    public static Utils getInstance() {
        return SingleTon.sInstance;
    }
    private Utils() {
    }

    public static Application getApp() {
        return application;
    }

    private static class SingleTon {
        private static final Utils sInstance = new Utils();
    }

    private static Application application;
    public static void init(Application application) {
      Utils.application = application;
    }

    public static Context getContext() {
        return getApp().getApplicationContext();
    }

/*    public  boolean isDebug() {
        return DebugHelper.getInstance().isDebug();
    }*/

    public static Locale getLocale() {
        return Locale.CHINA;
    }
}
