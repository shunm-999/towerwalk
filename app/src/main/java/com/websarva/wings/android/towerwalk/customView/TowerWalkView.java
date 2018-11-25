package com.websarva.wings.android.towerwalk.customView;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.websarva.wings.android.towerwalk.R;
import com.websarva.wings.android.towerwalk.callback.OnClickCallback;
import com.websarva.wings.android.towerwalk.consts.GameConst;
import com.websarva.wings.android.towerwalk.item.CharacterIcon;
import com.websarva.wings.android.towerwalk.util.BitmapResizer;

import java.util.ArrayList;
import java.util.List;

public abstract class TowerWalkView extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener, OnClickCallback {

    // TAG
    private static final String TAG = TowerWalkView.class.getSimpleName();

    // 全体の幅
    protected int mWidth;

    // 一辺のボックス数
    protected int mSquareNumber;
    // 盤面の状態を保持するリスト（行・列）
    protected int[][] mBoardTowerList;

    // タワーの一辺の長さ
    private int mTowerSquareLength;
    // タワーのビットマップのリスト
    private List<Bitmap> mBitmapTowerList;

    // プレイヤー用のビットマップ
    private Bitmap mBitmapPlayer;
    // プレイヤー用クラス
    protected CharacterIcon mPlayer;
    // 対戦相手用のビットマップ
    private Bitmap mBitmapOpponent;
    // 対戦相手用のクラス
    protected CharacterIcon mOpponent;

    // ボタンのリスト
    protected List<ImageView> mButtonList = new ArrayList<>();
    // ゲーム結果を表示するテキスト
    protected TextView mTextResult;

    /**
     * TowerWalkViewを構築する
     *
     * @param context      　コンテキスト
     * @param squareNumber 行数と列数
     * @param textResult   勝敗を表示するビュー
     * @param imageViews   操作ボタン
     */
    public TowerWalkView(Context context, int squareNumber, TextView textResult, ImageView... imageViews) {
        super(context);
        mSquareNumber = squareNumber;
        mTextResult = textResult;
        createBoardTowerList(squareNumber);

        // ボタンにコールバックをセットする
        for (ImageView imageView : imageViews) {
            mButtonList.add(imageView);
            imageView.setOnClickListener(this);
        }

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    /**
     * 盤面の状態を保持するリストを生成する
     */
    protected void createBoardTowerList(int squareNumber) {
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

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        // 幅・高さを取得
        mWidth = getWidth();

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
     * 描画領域に描画する
     */
    final protected void drawGameBoard() {

        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.BLACK);

        // タワーを描写する
        for (int i = 0; i < mBoardTowerList.length; i++) {
            for (int t = 0; t < mBoardTowerList[i].length; t++) {
                if (mBoardTowerList[i][t] != GameConst.OUTSIDE_TOWER_HIGH) {
                    canvas.drawBitmap(mBitmapTowerList.get(mBoardTowerList[i][t] - 1), mTowerSquareLength * (t - 1), mTowerSquareLength * (i - 1), null);
                }
            }
        }

        // プレイヤーと対戦相手を描写する
        canvas.drawBitmap(mBitmapPlayer, mPlayer.getLeft(), mPlayer.getTop(), null);
        canvas.drawBitmap(mBitmapOpponent, mOpponent.getLeft(), mOpponent.getTop(), null);

        getHolder().unlockCanvasAndPost(canvas);
    }

    /**
     * キャラクターアイコンを動かす
     *
     * @param characterIcon キャラクター
     * @param distance      距離
     * @return 処理の成功・失敗
     */
    final protected boolean movePlayerIcon(CharacterIcon characterIcon, int[] distance) {

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
        drawGameBoard();
        return true;
    }

    /**
     * ゲームの勝敗を描画する
     */
    protected void showGameResult(String result, TextView resultText) {
        resultText.setVisibility(VISIBLE);
        resultText.setText(result);
    }
}
