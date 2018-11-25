package com.websarva.wings.android.towerwalk.customView;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.algorithm.ReductionAndConquerAlgorithm;
import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.consts.KeyMapConst;
import com.websarva.wings.android.towerwalk.controller.Controller;
import com.websarva.wings.android.towerwalk.controller.GameController;
import com.websarva.wings.android.towerwalk.data.GameRecord;
import com.websarva.wings.android.towerwalk.db.TowerWalkDB;

public class TowerWalkGameBoardView extends TowerWalkView implements SurfaceHolder.Callback {

    // TAG
    private static final String TAG = TowerWalkGameBoardView.class.getSimpleName();

    // 対戦記録を保持するリスト
    private GameRecord mGameRecord = new GameRecord();
    // ゲームの制御コントローラー
    private Controller mGameController = new GameController();

    // ボタンの押下の不可を判定するフラグ
    private boolean mClickFrag = true;

    /**
     * TowerWalkGameBoardViewを構築する
     *
     * @param context      　コンテキスト
     * @param squareNumber 行数と列数
     * @param textResult   勝敗を表示するビュー
     * @param imageViews   操作ボタン
     */
    public TowerWalkGameBoardView(Context context, int squareNumber, TextView textResult, ImageView... imageViews) {
        super(context, squareNumber, textResult, imageViews);
    }

    /**
     * ボタンが押下されたことを通知する
     */
    private GameConst.GameStatus notifyChanged(KeyMapConst.KeyMap keyMap) {

        // ゲームの進行状況を表す変数
        GameConst.GameStatus currentGameStatus;
        int[] playerDistance = keyMap.getDistance();

        if (!movePlayerIcon(mPlayer, playerDistance)) {
            return GameConst.GameStatus.ERROR;
        }
        drawGameBoard();
        // 差し手を保存する
        mGameRecord.append(playerDistance);

        // ゲームの進行状況を判定する
        currentGameStatus = mGameController.judgeGame(mPlayer, mOpponent, mBoardTowerList);
        if (currentGameStatus != GameConst.GameStatus.PLAYING) return currentGameStatus;

        int[] opponentDistance = new ReductionAndConquerAlgorithm().decideNextPosition(mBoardTowerList, mOpponent, mPlayer);

        if (!movePlayerIcon(mOpponent, opponentDistance)) {
            return GameConst.GameStatus.ERROR;
        }
        drawGameBoard();
        // 差し手を保存する
        mGameRecord.append(opponentDistance);

        // ゲームの進行状況を判定する
        currentGameStatus = mGameController.judgeGame(mPlayer, mOpponent, mBoardTowerList);
        if (currentGameStatus != GameConst.GameStatus.PLAYING) return currentGameStatus;

        return currentGameStatus;
    }

    @Override
    public void onClick(View view) {
        if (!mClickFrag) {
            return;
        }

        // 押下不可にする
        mClickFrag = false;
        ImageView button = null;
        try {
            button = (ImageView) view;
        } catch (ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }

        // 押されたキーの種別
        KeyMapConst.KeyMap clickButtonKey = null;
        switch (button.getTag().toString()) {
            case KeyMapConst.BUTTON_LEFT:
                clickButtonKey = KeyMapConst.KeyMap.LEFT;
                break;
            case KeyMapConst.BUTTON_TOP:
                clickButtonKey = KeyMapConst.KeyMap.TOP;
                break;
            case KeyMapConst.BUTTON_RIGHT:
                clickButtonKey = KeyMapConst.KeyMap.RIGHT;
                break;
            case KeyMapConst.BUTTON_BOTTOM:
                clickButtonKey = KeyMapConst.KeyMap.BOTTOM;
                break;
        }

        if (clickButtonKey == null) {
            return;
        }
        this.onCall(notifyChanged(clickButtonKey));

        for (ImageView btn : mButtonList) {
            btn.setEnabled(true);
        }

        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            int[] nextPosition = new int[]{mPlayer.getCurrentPosition()[0] + keyMap.getDistance()[0], mPlayer.getCurrentPosition()[1] + keyMap.getDistance()[1]};
            int currentPositionHigh = mBoardTowerList[mPlayer.getCurrentPosition()[0]][mPlayer.getCurrentPosition()[1]];
            int nextPositionHigh = mBoardTowerList[nextPosition[0]][nextPosition[1]];

            if ((nextPositionHigh == GameConst.OUTSIDE_TOWER_HIGH) || (nextPositionHigh >= GameConst.MAX_TOWER_HIGH) || Math.abs(currentPositionHigh - nextPositionHigh) > GameConst.DIFFERENCE_IN_HEIGHT) {
                for (ImageView btn : mButtonList) {
                    if (TextUtils.equals(btn.getTag().toString(), keyMap.getStatus())) {
                        btn.setEnabled(false);
                    }
                }
            }
        }
        mClickFrag = true;
    }

    @Override
    public void onCall(GameConst.GameStatus gameStatus) {
        if (gameStatus != GameConst.GameStatus.PLAYING) {
            showGameResult(getResources().getString(gameStatus.getResultTextCode()), mTextResult);
            for (ImageView imageView : mButtonList) {
                imageView.setOnClickListener(null);
            }
            TowerWalkDB.recordResult(gameStatus, mGameRecord);
        }
    }
}
