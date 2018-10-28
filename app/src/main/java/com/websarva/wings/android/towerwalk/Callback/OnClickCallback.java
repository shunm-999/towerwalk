package com.websarva.wings.android.towerwalk.Callback;

import com.websarva.wings.android.towerwalk.CustomView.TowerWalkBoardView;

public abstract class OnClickCallback {


    /**
     * 描画処理が終了した後にコールされるメソッド
     */
    public abstract void onCall(TowerWalkBoardView.GameStatus gameStatus);
}
