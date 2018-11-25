package com.websarva.wings.android.towerwalk.callback;

import com.websarva.wings.android.towerwalk.consts.GameConst;

public interface OnClickCallback {

    /**
     * 描画処理が終了した後にコールされるメソッド
     */
    void onCall(GameConst.GameStatus gameStatus);
}
