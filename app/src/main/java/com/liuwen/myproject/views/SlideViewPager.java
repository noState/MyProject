package com.liuwen.myproject.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-07-24
 * <p>
 * <p>
 * <p>
 * 是否可以左右滑动的ViewPager
 */

public class SlideViewPager extends ViewPager {

    protected boolean isCanScroll = true;

    public SlideViewPager(Context context) {
        super(context);
    }

    public SlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return isCanScroll && super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return isCanScroll && super.onInterceptTouchEvent(arg0);
    }
}
