package com.websarva.wings.android.towerwalk.fragment;


import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.customView.TowerWalkGameResultView;
import com.websarva.wings.android.towerwalk.db.Database;
import com.websarva.wings.android.towerwalk.db.TowerWalkDB;

/**
 * 対戦記録詳細画面
 */
public class GameResultDetailFragment extends BaseFragment implements TowerWalkGameResultView.OnClickCallBack {

    // レイアウトの指定（親のコンテンツ幅に合わせる）
    private static final int MP = ViewGroup.LayoutParams.MATCH_PARENT;

    // 対戦結果
    private Database.ResultSet mResultSet = new Database.ResultSet();

    // 次に進むボタン
    private ImageView mImageNextButton;
    // 前に戻るボタン
    private ImageView mImageBackButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_game_result_detail, container, false);
        getExtras();
        setupLayout(contentView);
        return contentView;
    }

    /**
     * 遷移元からの情報を取得する
     */
    private void getExtras() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        int sequenceId = bundle.getInt(BaseColumns._ID);
        mResultSet = TowerWalkDB.getGameResultDetailData(sequenceId);
    }

    @Override
    protected void setupLayout(View contentView) {
        LinearLayout boardWrapperLayout = contentView.findViewById(R.id.board_wrapper_layout);
        boardWrapperLayout.removeAllViews();

        // 操作ボタン
        mImageNextButton = contentView.findViewById(R.id.button_record_next);
        mImageBackButton = contentView.findViewById(R.id.button_record_back);
        mImageBackButton.setEnabled(false);

        // ゲーム結果を表示するビュー
        TextView textGameResult = contentView.findViewById(R.id.text_game_result);
        textGameResult.setVisibility(View.GONE);

        // 対戦結果を再生するカスタムビュー
        TowerWalkGameResultView towerWalkGameResultView = new TowerWalkGameResultView(
                getContext(),
                GameConst.SQUARE_NUMBER,
                textGameResult,
                mResultSet,
                mImageNextButton,
                mImageBackButton);
        towerWalkGameResultView.registerCallBack(this);
        boardWrapperLayout.addView(towerWalkGameResultView, new LinearLayout.LayoutParams(MP, MP));
    }

    @Override
    public void onCompletion(int pointer) {
        mImageBackButton.setEnabled(true);
        mImageNextButton.setEnabled(true);
        if (pointer == 0) {
            mImageBackButton.setEnabled(false);
        } else if (pointer >= mResultSet.size()) {
            mImageNextButton.setEnabled(false);
        }
    }
}
