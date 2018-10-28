package com.websarva.wings.android.towerwalk.CustomView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.websarva.wings.android.towerwalk.Algorithm.ReductionAndConquerAlgorithm;
import com.websarva.wings.android.towerwalk.Callback.OnClickCallback;
import com.websarva.wings.android.towerwalk.Const.GameConst;
import com.websarva.wings.android.towerwalk.Const.KeyMapConst;
import com.websarva.wings.android.towerwalk.Item.CharacterIcon;
import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.Util.BitmapResizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TowerWalkBoardView extends SurfaceView implements SurfaceHolder.Callback {

    // TAG
    private static final String TAG = TowerWalkBoardView.class.getSimpleName();

    // ゲームの勝敗
    public enum GameStatus {
        PLAYING,
        DRAW,
        WIN,
        LOSE,
        ERROR
    }

    // 全体の幅
    private int mWidth;
    // 全体の高さ
    private int mHeight;

    // 一辺のボックス数
    private int mSquareNumber;

    // 盤面の状態を保持するリスト（行・列）
    private int[][] mBoardTowerList;

    // タワーの一辺の長さ
    private int mTowerSquareLength;
    // タワーのビットマップのリスト
    private List<Bitmap> mBitmapTowerList;

    // プレイヤー用のビットマップ
    private Bitmap mBitmapPlayer;
    // プレイヤー用クラス
    private CharacterIcon mPlayer;
    // 対戦相手用のビットマップ
    private Bitmap mBitmapOpponent;
    // 対戦相手用のクラス
    private CharacterIcon mOpponent;

    // ホルダー
    private SurfaceHolder mHolder;
    // キャンバス
    private Canvas mCanvas = null;
    // ボタンの押下の不可を判定するフラグ
    private boolean mClickFrag = true;

    public TowerWalkBoardView(Context context, int squareNumber, ImageView... imageViews) {
        super(context);
        // 盤面リストの生成
        mSquareNumber = squareNumber;
        createBoardTowerList(squareNumber);

        // 操作用のボタンを登録
        OnClickListener clickListener = new OnClickListener(new OnClickCallback() {
            @Override
            public void onCall(GameStatus gameStatus) {

            }
        }, imageViews);

        // ボタンにコールバックをセットする
        for (ImageView imageView : imageViews) {
            imageView.setOnClickListener(clickListener);
        }

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        // 幅・高さを取得
        mWidth = getWidth();
        mHeight = getHeight();

        // タワーの一辺の長さを決定
        mTowerSquareLength = mWidth / mSquareNumber;

        Resources resources = getResources();
        // プレイヤー用のビットマップを用意する
        mBitmapPlayer = BitmapResizer.resize(resources, R.drawable.player_red, (int) (mTowerSquareLength * 0.8), (int) (mTowerSquareLength * 0.8));
        // プレーヤー用クラスを用意する
        mPlayer = new CharacterIcon(mBitmapPlayer.getWidth(), mBitmapPlayer.getHeight(), new int[]{mSquareNumber, 1}, mTowerSquareLength);
        // 対戦相手用のビットマップを用意する
        mBitmapOpponent = BitmapResizer.resize(resources, R.drawable.player_blue, (int) (mTowerSquareLength * 0.8), (int) (mTowerSquareLength * 0.8));
        // 対戦相手用のクラスを用意する
        mOpponent = new CharacterIcon(mBitmapOpponent.getWidth(), mBitmapOpponent.getHeight(), new int[]{1, mSquareNumber}, mTowerSquareLength);

        // タワーのビットマップを用意する
        mBitmapTowerList = new ArrayList<>();
        TypedArray towers = resources.obtainTypedArray(R.array.tower_id_list);
        for (int i = 0; i < towers.length(); i++) {
            mBitmapTowerList.add(BitmapResizer.resize(resources, towers.getResourceId(i, -1), mTowerSquareLength, mTowerSquareLength));
        }
        towers.recycle();

        drawGameBoard();
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Bitmapリソースをメモリから解放する
        if (mBitmapPlayer != null) {
            mBitmapPlayer.recycle();
            mBitmapPlayer = null;
        }

        if (mBitmapOpponent != null) {
            mBitmapOpponent.recycle();
            mBitmapOpponent = null;
        }

        for (Bitmap tower : mBitmapTowerList) {
            if (tower != null) {
                tower.recycle();
            }
        }
        mBitmapTowerList = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = widthSize;

        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 盤面の状態を保持するリストを生成する
     */
    private void createBoardTowerList(int squareNumber) {
        int N = squareNumber + 2;
        mBoardTowerList = new int[N][N];
        for (int i = 0; i < N; i++) {
            mBoardTowerList[0][i] = GameConst.OUTSIDE_TOWER_HIGH;
            mBoardTowerList[N - 1][i] = GameConst.OUTSIDE_TOWER_HIGH;
            mBoardTowerList[i][0] = GameConst.OUTSIDE_TOWER_HIGH;
            mBoardTowerList[i][N - 1] = GameConst.OUTSIDE_TOWER_HIGH;
        }

        for (int i = 0; i < N; i++) {
            for (int t = 0; t < N; t++) {
                mBoardTowerList[i][t] = (mBoardTowerList[i][t] != GameConst.OUTSIDE_TOWER_HIGH) ? 1 : GameConst.OUTSIDE_TOWER_HIGH;
            }
        }
    }

    /**
     * ボタンが押下されたことを通知する
     */
    private GameStatus notifyChanged(KeyMapConst.KeyMap keyMap) {
        GameStatus currentGameStatus = GameStatus.PLAYING;
        int[] playerDistance = keyMap.getDistance();
        if (!movePlayerIcon(mPlayer, playerDistance)) {
            return GameStatus.ERROR;
        }
        drawGameBoard();

        currentGameStatus = judgeGame();
        if (currentGameStatus != GameStatus.PLAYING) return currentGameStatus;

        int[] opponentDistance = new ReductionAndConquerAlgorithm().decideNextPosition(mBoardTowerList, mOpponent, mPlayer);
        if (!movePlayerIcon(mOpponent, opponentDistance)) {
            return GameStatus.ERROR;
        }
        drawGameBoard();

        currentGameStatus = judgeGame();
        if (currentGameStatus != GameStatus.PLAYING) return currentGameStatus;

        mClickFrag = true;

        return currentGameStatus;
    }

    /**
     * ゲームの勝敗を判定する
     */
    private GameStatus judgeGame() {

        boolean playerCanMove = false;
        boolean opponentCanMove = false;

        for (KeyMapConst.KeyMap keyMap : KeyMapConst.KeyMap.values()) {
            int[] playerPosition = new int[]{mPlayer.getCurrentPosition()[0] + keyMap.getDistance()[0], mPlayer.getCurrentPosition()[1] + keyMap.getDistance()[1]};
            int playerCurrentPositionHigh = mBoardTowerList[mPlayer.getCurrentPosition()[0]][mPlayer.getCurrentPosition()[1]];
            int playerPositionHigh = mBoardTowerList[playerPosition[0]][playerPosition[1]];

            int[] opponentPosition = new int[]{mOpponent.getCurrentPosition()[0] + keyMap.getDistance()[0], mOpponent.getCurrentPosition()[1] + keyMap.getDistance()[1]};
            int opponentCurrentPositionHigh = mBoardTowerList[mOpponent.getCurrentPosition()[0]][mOpponent.getCurrentPosition()[1]];
            int opponentPositionHigh = mBoardTowerList[opponentPosition[0]][opponentPosition[1]];

            if ((playerPositionHigh != GameConst.OUTSIDE_TOWER_HIGH) && (playerPositionHigh < GameConst.MAX_TOWER_HIGH) && Math.abs(playerCurrentPositionHigh - playerPositionHigh) <= GameConst.DIFFERENCE_IN_HEIGHT) {
                playerCanMove = true;
            }

            if ((opponentPositionHigh != GameConst.OUTSIDE_TOWER_HIGH) && (opponentPositionHigh < GameConst.MAX_TOWER_HIGH) && Math.abs(opponentCurrentPositionHigh - opponentPositionHigh) <= GameConst.DIFFERENCE_IN_HEIGHT) {
                opponentCanMove = true;
            }
        }

        if (!playerCanMove && !opponentCanMove) {
            return GameStatus.DRAW;
        } else if (!playerCanMove) {
            return GameStatus.LOSE;
        } else if (!opponentCanMove) {
            return GameStatus.WIN;
        }

        return GameStatus.PLAYING;
    }

    /**
     * 描画領域に描画する
     */
    private void drawGameBoard() {

        mCanvas = getHolder().lockCanvas();
        mCanvas.drawColor(Color.GRAY);

        // タワーを描写する
        for (int i = 0; i < mBoardTowerList.length; i++) {
            for (int t = 0; t < mBoardTowerList[i].length; t++) {
                if (mBoardTowerList[i][t] != GameConst.OUTSIDE_TOWER_HIGH) {
                    mCanvas.drawBitmap(mBitmapTowerList.get(mBoardTowerList[i][t] - 1), mTowerSquareLength * (t - 1), mTowerSquareLength * (i - 1), null);
                }
            }
        }

        // プレイヤーと対戦相手を描写する
        mCanvas.drawBitmap(mBitmapPlayer, mPlayer.getLeft(), mPlayer.getTop(), null);
        mCanvas.drawBitmap(mBitmapOpponent, mOpponent.getLeft(), mOpponent.getTop(), null);

        getHolder().unlockCanvasAndPost(mCanvas);
    }

    /**
     * キャラクターアイコンを動かす
     */
    private boolean movePlayerIcon(CharacterIcon characterIcon, int[] distance) {

        int currentPositionHigh = mBoardTowerList[characterIcon.getCurrentPosition()[0]][characterIcon.getCurrentPosition()[1]];
        int[] nextPosition = new int[]{characterIcon.getCurrentPosition()[0] + distance[0], characterIcon.getCurrentPosition()[1] + distance[1]};
        int nextPositionHigh = mBoardTowerList[nextPosition[0]][nextPosition[1]];

        if ((nextPositionHigh == GameConst.OUTSIDE_TOWER_HIGH) || (nextPositionHigh >= GameConst.MAX_TOWER_HIGH) || Math.abs(currentPositionHigh - nextPositionHigh) > GameConst.DIFFERENCE_IN_HEIGHT) {
            return false;
        }

        int nextCenterX = mTowerSquareLength * (nextPosition[1] - 1) + (mTowerSquareLength / 2);
        int nextCenterY = mTowerSquareLength * (nextPosition[0] - 1) + (mTowerSquareLength / 2);
        while ((Math.abs(characterIcon.getCenterX() - nextCenterX) >= 5) || (Math.abs(characterIcon.getCenterY() - nextCenterY) >= 5)) {
            characterIcon.move(distance[1] * 5, distance[0] * 5);
            // 画面を更新
            drawGameBoard();
        }

        // 位置を更新
        characterIcon.setCurrentPosition(nextPosition);
        mBoardTowerList[nextPosition[0]][nextPosition[1]] += 1;
        return true;
    }

    private class OnClickListener implements View.OnClickListener {

        // ボタンのリスト
        private List<ImageView> mButtonList = new ArrayList<>();
        // ボタンを押下時のコールバック
        private OnClickCallback mCallback;

        private OnClickListener(OnClickCallback callback, ImageView... imageViews) {
            mButtonList.addAll(Arrays.asList(imageViews));
            mCallback = callback;
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

            switch (button.getTag().toString()) {
                case KeyMapConst.BUTTON_LEFT:
                    mCallback.onCall(notifyChanged(KeyMapConst.KeyMap.LEFT));
                    break;
                case KeyMapConst.BUTTON_TOP:
                    mCallback.onCall(notifyChanged(KeyMapConst.KeyMap.TOP));
                    break;
                case KeyMapConst.BUTTON_RIGHT:
                    mCallback.onCall(notifyChanged(KeyMapConst.KeyMap.RIGHT));
                    break;
                case KeyMapConst.BUTTON_BOTTOM:
                    mCallback.onCall(notifyChanged(KeyMapConst.KeyMap.BOTTOM));
                    break;
            }

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
        }
    }
}
