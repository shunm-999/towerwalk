package com.websarva.wings.android.towerwalk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.activity.GameActivity;
import com.websarva.wings.android.towerwalk.activity.GameResultActivity;

/**
 * ホーム画面
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);
        setupLayout(contentView);
        return contentView;
    }

    @Override
    public void setupLayout(View contentView) {
        // 対戦画面に遷移するボタン
        ImageView imageButtonBattle = contentView.findViewById(R.id.image_battle);
        imageButtonBattle.setOnClickListener(this);
        // 対戦記録画面に遷移するボタン
        ImageView imageButtonMemory = contentView.findViewById(R.id.image_memory);
        imageButtonMemory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.image_battle:
                // 対戦画面に遷移するボタン
                intent = new Intent(getActivity(), GameActivity.class);
                break;
            case R.id.image_memory:
                // 対戦記録画面に遷移するボタン
                intent = new Intent(getActivity(), GameResultActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
