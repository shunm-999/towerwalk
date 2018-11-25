package com.websarva.wings.android.towerwalk.fragment;


import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
    private Database.ResultSet mGameResultSet = new Database.ResultSet();
    // 対戦結果詳細
    private Database.ResultSet mGameResultDetailSet = new Database.ResultSet();

    // 再生画面
    private TowerWalkGameResultView mTowerWalkGameResultView;
    // プログラスを表示するテキスト
    private TextView mTextProgress;
    // シークバー
    private SeekBar mSeekBar;

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
        mGameResultSet = TowerWalkDB.getGameResultData(sequenceId);
        mGameResultDetailSet = TowerWalkDB.getGameResultDetailData(sequenceId);
    }

    @Override
    protected void setupLayout(View contentView) {// 画面タイトルをセット
        TextView textBarTitle = contentView.findViewById(R.id.text_bar_title);
        if (textBarTitle != null) {
            textBarTitle.setText(R.string.fragment_game_result_detail_title);
        }

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
        mTowerWalkGameResultView = new TowerWalkGameResultView(
                getContext(),
                GameConst.SQUARE_NUMBER,
                textGameResult,
                mGameResultDetailSet,
                mImageNextButton,
                mImageBackButton);
        mTowerWalkGameResultView.registerCallBack(this);
        boardWrapperLayout.addView(mTowerWalkGameResultView, new LinearLayout.LayoutParams(MP, MP));

        TextView textMax = contentView.findViewById(R.id.text_record_max);
        textMax.setText(String.valueOf(mGameResultDetailSet.size()));
        mTextProgress = contentView.findViewById(R.id.text_record_progress);

        mSeekBar = contentView.findViewById(R.id.seek_bar_game_result);
        mSeekBar.setMax(mGameResultDetailSet.size());
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    @Override
    public void onCompletion(int pointer) {
        mImageBackButton.setEnabled(true);
        mImageNextButton.setEnabled(true);
        if (pointer == 0) {
            mImageBackButton.setEnabled(false);
        } else if (pointer >= mGameResultDetailSet.size()) {
            mImageNextButton.setEnabled(false);
        }

        mSeekBar.setProgress(pointer);
        mTextProgress.setText(String.valueOf(pointer));
    }

    /**
     * シークバーの状態変化のリスナー
     */
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
            if (fromUser) {
                int pointer = mTowerWalkGameResultView.getPointer();
                int count = Math.abs(position - pointer);

                for (int i = 0; i < count; i++) {
                    if (position > pointer) {
                        mTowerWalkGameResultView.setPlayerIcon(true);
                    } else if (position < pointer) {
                        mTowerWalkGameResultView.setPlayerIcon(false);
                    }
                }
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
