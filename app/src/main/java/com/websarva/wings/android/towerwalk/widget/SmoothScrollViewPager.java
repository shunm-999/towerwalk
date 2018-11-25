package com.websarva.wings.android.towerwalk.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * スムーズにスクロールするViewPager
 */
public class SmoothScrollViewPager extends ViewPager {

    // スクロールの秒数
    private static final int SCROLL_DURATION = 500;

    /**
     * SmoothScrollViewPagerを構築する
     *
     * @param context コンテキスト
     */
    public SmoothScrollViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    /**
     * SmoothScrollViewPagerを構築する
     *
     * @param context コンテキスト
     * @param attr    属性群
     */
    public SmoothScrollViewPager(Context context, AttributeSet attr) {
        super(context, attr);
        setMyScroller();
    }

    /**
     * スクロールの制御クラスをセットするs
     */
    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * スクロールの制御クラス
     */
    private static class MyScroller extends Scroller {

        /**
         * MyScrollerを構築する
         *
         * @param context コンテキスト
         */
        private MyScroller(Context context) {
            super(context, new FastOutSlowInInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, SCROLL_DURATION);
        }
    }
}