package com.websarva.wings.android.towerwalk;

import android.app.Application;
import android.content.Context;

public class TowerWalkApplication extends Application {
    private static TowerWalkApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static TowerWalkApplication getInstance() {
        return instance;
    }
}
