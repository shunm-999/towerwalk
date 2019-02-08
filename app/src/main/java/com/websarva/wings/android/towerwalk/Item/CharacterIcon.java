package com.websarva.wings.android.towerwalk.item;

public class CharacterIcon extends GameObject {

    // 現在位置
    private int[] mPosition;
    // Towerの一辺の長さ
    private int mTowerSquareLength;

    /**
     * PlayerIconを構築する
     */
    public CharacterIcon(int width, int height, int[] position, int towerSquareLength) {
        super(0, 0, width, height);
        mPosition = position;
        mTowerSquareLength = towerSquareLength;
        setLocateFromPosition(mPosition);
    }

    /**
     * 位置を決定する
     *
     * @param centerX 中心のX座標
     * @param centerY 中心のY座標
     */
    private void setLocateCenter(int centerX, int centerY) {
        this.mLeft = centerX - (this.mWidth / 2);
        this.mTop = centerY - (this.mHeight / 2);
    }

    /**
     * 現在位置から位置を決定する
     *
     * @param position 現在位置
     */
    private void setLocateFromPosition(int[] position) {
        setLocateCenter((position[1] - 1) * mTowerSquareLength + (mTowerSquareLength / 2), (position[0] - 1) * mTowerSquareLength + (mTowerSquareLength / 2));
    }

    /**
     * 現在位置を取得する
     *
     * @return 現在位置
     */
    public int[] getCurrentPosition() {
        return mPosition;
    }

    /**
     * 現在位置を設定する
     *
     * @param position 現在位置
     */
    public void setCurrentPosition(int[] position) {
        this.mPosition = position;
        setLocateFromPosition(position);
    }
}
