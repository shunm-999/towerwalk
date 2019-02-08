package com.websarva.wings.android.towerwalk.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.websarva.wings.android.towerwalk.R;

/**
 * 全てのフラグメントの親
 */
public abstract class BaseFragment extends Fragment {

    /**
     * レイアウトをセットする
     *
     * @param contentView xmlからインフレートされたレイアウト
     */
    protected abstract void setupLayout(View contentView);

    @Override
    public void onStart() {
        super.onStart();
        View backButton = getView().findViewById(R.id.image_back_button);
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        }
    }
}
