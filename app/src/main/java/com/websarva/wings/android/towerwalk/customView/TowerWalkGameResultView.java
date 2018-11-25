package com.websarva.wings.android.towerwalk.customView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.db.CompetitionHistoryDetailTable;
import com.websarva.wings.android.towerwalk.db.Database;
import com.websarva.wings.android.towerwalk.item.CharacterIcon;

public class TowerWalkGameResultView extends TowerWalkView {

    // 対戦記録詳細
    private Database.ResultSet mResultSet;
    // ポインター
    private int mPointer = 0;
    // コールバック
    private OnClickCallBack mCallBack;

    /**
     * TowerWalkGameResultViewを構築する
     *
     * @param context      　コンテキスト
     * @param squareNumber 行数と列数
     * @param textResult   勝敗を表示するビュー
     * @param resultSet    対戦結果
     * @param imageViews   操作ボタン
     */
    public TowerWalkGameResultView(Context context, int squareNumber, TextView textResult, Database.ResultSet resultSet, ImageView... imageViews) {
        super(context, squareNumber, textResult, imageViews);
        mResultSet = resultSet;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_record_next:
                // 一つ前に進む
                setPlayerIcon(true);
                break;
            case R.id.button_record_back:
                // 一つ後ろに戻る
                setPlayerIcon(false);
                break;
        }
    }

    /**
     * プレイヤーアイコンを動かす
     *
     * @param proceed true:進む false:戻る
     */
    public void setPlayerIcon(boolean proceed) {

        if ((mPointer == mResultSet.size() && proceed) || (mPointer == 0 && !proceed)) {
            // 不正な値の場合は、処理を行わない
            return;
        }
        Database.Result result = proceed ? mResultSet.get(mPointer) : mResultSet.get(mPointer - 1);
        CharacterIcon characterIcon = (result.getInt(CompetitionHistoryDetailTable.COLUMN_TURN) == 0) ? mPlayer : mOpponent;

        int[] nextPosition = new int[2];
        int line = result.getInt(CompetitionHistoryDetailTable.COLUMN_LINE);
        int row = result.getInt(CompetitionHistoryDetailTable.COLUMN_ROW);

        if (proceed) {
            nextPosition[0] = characterIcon.getCurrentPosition()[0] + line;
            nextPosition[1] = characterIcon.getCurrentPosition()[1] + row;
            mBoardTowerList[nextPosition[0]][nextPosition[1]]++;
        } else {
            nextPosition[0] = characterIcon.getCurrentPosition()[0] - line;
            nextPosition[1] = characterIcon.getCurrentPosition()[1] - row;
            mBoardTowerList[characterIcon.getCurrentPosition()[0]][characterIcon.getCurrentPosition()[1]]--;
        }
        characterIcon.setCurrentPosition(nextPosition);

        mPointer = proceed ? mPointer + 1 : mPointer - 1;
        drawGameBoard();

        mCallBack.onCompletion(mPointer);
    }

    /**
     * 現在の再生位置を取得する
     *
     * @return 現在の再生位置
     */
    public int getPointer() {
        return mPointer;
    }

    /**
     * 描写処理が終わった後に呼ばれるコールバック
     */
    public interface OnClickCallBack {
        void onCompletion(int pointer);
    }

    /**
     * コールバックを登録する
     *
     * @param callBack コールバック
     */
    public void registerCallBack(OnClickCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void onCall(GameConst.GameStatus gameStatus) {

    }
}
