package com.websarva.wings.android.towerwalk.callback;

import com.websarva.wings.android.towerwalk.consts.GameConst;

public abstract class OnClickCallback {


    /**
     * 描画処理が終了した後にコールされるメソッド
     */
    public abstract void onCall(GameConst.GameStatus gameStatus);
}
