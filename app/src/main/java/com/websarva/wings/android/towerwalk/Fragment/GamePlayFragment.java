package com.websarva.wings.android.towerwalk.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.websarva.wings.android.towerwalk.CustomView.TowerWalkBoardView;
import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.Const.KeyMapConst;

public class GamePlayFragment extends Fragment {

    private static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_game_play, container);
        setupLayout(contentView);
        return contentView;
    }

    public void setupLayout(View contentView) {
        LinearLayout boardWrapperLayout = contentView.findViewById(R.id.board_wrapper_layout);

        // 操作用のボタンを取得
        ImageView leftButton = contentView.findViewById(R.id.left_button);
        leftButton.setTag(KeyMapConst.BUTTON_LEFT);
        leftButton.setEnabled(false);
        ImageView topButton = contentView.findViewById(R.id.top_button);
        topButton.setTag(KeyMapConst.BUTTON_TOP);
        ImageView rightButton = contentView.findViewById(R.id.right_button);
        rightButton.setTag(KeyMapConst.BUTTON_RIGHT);
        ImageView bottomButton = contentView.findViewById(R.id.bottom_button);
        bottomButton.setTag(KeyMapConst.BUTTON_BOTTOM);
        bottomButton.setEnabled(false);

        TowerWalkBoardView towerWalkBoardView = new TowerWalkBoardView(getContext(), 5, leftButton, topButton, rightButton, bottomButton);

        boardWrapperLayout.addView(towerWalkBoardView, new LinearLayout.LayoutParams(MP, MP));

    }
}
