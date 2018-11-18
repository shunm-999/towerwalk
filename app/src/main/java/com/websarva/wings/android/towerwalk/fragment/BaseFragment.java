package com.websarva.wings.android.towerwalk.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 全てのフラグメントの親
 */
public abstract class BaseFragment extends Fragment {

    /**
     * レイアウトをセットする
     *
     * @param contentView xmlからインフレートされたレイアウト
     */
    public abstract void setupLayout(View contentView);
}
