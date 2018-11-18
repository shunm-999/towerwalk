package com.websarva.wings.android.towerwalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.websarva.wings.android.towerwalk.R;

/**
 * スプラッシュ画面
 */
public class SplashActivity extends BaseActivity {

    // 待機秒数
    private static final int DELAY_TIME = 3000;
    // ハンドラー
    private Handler mHandler = new Handler();
    // 画面遷移タスク
    private Runnable transitionTask = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(transitionTask, DELAY_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(transitionTask);
    }
}
