package com.websarva.wings.android.towerwalk.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.consts.KeyMapConst;
import com.websarva.wings.android.towerwalk.customView.TowerWalkGameBoardView;

/**
 * 対戦画面
 */
public class GamePlayFragment extends BaseFragment {

    // レイアウトの指定（親のコンテンツ幅に合わせる）
    private static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_game_play, container);
        setupLayout(contentView);
        return contentView;
    }

    @Override
    protected void setupLayout(View contentView) {
        LinearLayout boardWrapperLayout = contentView.findViewById(R.id.board_wrapper_layout);
        boardWrapperLayout.removeAllViews();

        // 操作用のボタンを取得
        ImageView leftButton = contentView.findViewById(R.id.left_button);
        leftButton.setTag(KeyMapConst.BUTTON_LEFT);
        leftButton.setEnabled(false);
        ImageView topButton = contentView.findViewById(R.id.top_button);
        topButton.setEnabled(true);
        topButton.setTag(KeyMapConst.BUTTON_TOP);
        ImageView rightButton = contentView.findViewById(R.id.right_button);
        rightButton.setTag(KeyMapConst.BUTTON_RIGHT);
        rightButton.setEnabled(true);
        ImageView bottomButton = contentView.findViewById(R.id.bottom_button);
        bottomButton.setTag(KeyMapConst.BUTTON_BOTTOM);
        bottomButton.setEnabled(false);

        // リセットボタンのリスナをセット
        ImageView resetButton = contentView.findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new ResetButtonClickListener());

        // 結果表示用のテキストビュー
        TextView resultText = contentView.findViewById(R.id.text_game_result);
        resultText.setVisibility(View.GONE);

        TowerWalkGameBoardView towerWalkBoardView = new TowerWalkGameBoardView(getContext(), GameConst.SQUARE_NUMBER, resultText, leftButton, topButton, rightButton, bottomButton);

        boardWrapperLayout.addView(towerWalkBoardView, new LinearLayout.LayoutParams(MP, MP));
    }

    /**
     * リセットボタン押下時のリスナ
     */
    private class ResetButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            setupLayout(getView());
        }
    }
}
