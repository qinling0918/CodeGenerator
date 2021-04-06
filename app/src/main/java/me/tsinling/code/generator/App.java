package me.tsinling.code.generator;

import android.app.Application;

import me.tsinling.code.generator.utils.Utils;


/**
 * created by tsinling on: 2020-03-09 16:43
 * description:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
