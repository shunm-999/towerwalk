package com.websarva.wings.android.towerwalk.activity;

import android.os.Bundle;

import com.websarva.wings.android.towerwalk.R;

/**
 * ホーム画面
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
