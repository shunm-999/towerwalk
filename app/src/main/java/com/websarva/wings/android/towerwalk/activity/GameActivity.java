package com.websarva.wings.android.towerwalk.activity;

import android.os.Bundle;

import com.websarva.wings.android.towerwalk.R;

/**
 * 対戦画面
 */
public class GameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
