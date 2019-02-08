package com.websarva.wings.android.towerwalk.item;

public class GameObject {

    // オブジェクトの左端
    protected int mLeft;
    // オブジェクトの上端
    protected int mTop;
    // オブジェクトの幅
    protected int mWidth;
    // オブジェクトの高さ
    protected int mHeight;

    /**
     * GameObjectを構築する
     */
    public GameObject(int left, int top, int width, int height) {
        setLocate(left, top);
        this.mWidth = width;
        this.mHeight = height;
    }

    /**
     * 位置を決定する
     *
     * @param left 左端
     * @param top  上端
     */
    public void setLocate(int left, int top) {
        this.mLeft = left;
        this.mTop = top;
    }

    /**
     * 位置を変更する
     *
     * @param x 横方向の移動距離
     * @param y 縦方向の移動距離
     */
    public void move(int x, int y) {
        this.mLeft = getLeft() + x;
        this.mTop = getTop() + y;
    }

    /**
     * オブジェクトの左端の座標を取得する
     *
     * @return 左端の座標
     */
    public int getLeft() {
        return mLeft;
    }

    /**
     * オブジェクトの右端の座標を取得する
     *
     * @return 右端の座標
     */
    public int getRight() {
        return mLeft + mWidth;
    }

    /**
     * オブジェクトの上端の座標を取得する
     *
     * @return 上端の座標
     */
    public int getTop() {
        return mTop;
    }

    /**
     * オブジェクトの下端の座標を取得する
     *
     * @return 下端の座標
     */
    public int getBottom() {
        return mTop + mHeight;
    }

    /**
     * オブジェクトの横方向の中心を取得する
     *
     * @return 横方向の中心
     */
    public int getCenterX() {
        return getLeft() + (mWidth / 2);
    }

    /**
     * オブジェクトの縦方向の中心を取得する
     *
     * @return 縦方向の中心
     */
    public int getCenterY() {
        return getTop() + (mHeight / 2);
    }

}
