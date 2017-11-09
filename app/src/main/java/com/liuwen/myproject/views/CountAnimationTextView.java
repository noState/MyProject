package com.liuwen.myproject.views;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.text.DecimalFormat;

/**
 * Created by Liuwen (WX:LW993209378).
 * on 2017-08-22
 * <p>
 * 动画效果的示数TextView
 */

public class CountAnimationTextView extends AppCompatTextView {

    private boolean isAnimating = false;

    private ValueAnimator mCountAnimator;

    private CountAnimationListener mCountAnimationListener;

    private DecimalFormat mDecimalFormat;

    private static final long DEFAULT_DURATION = 1000;

    public CountAnimationTextView(Context context) {
        this(context, null, 0);
    }

    public CountAnimationTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountAnimationTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpAnimator();
    }

    private void setUpAnimator() {
        mCountAnimator = new ValueAnimator();
        mCountAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String value;
                if (mDecimalFormat == null) {
                    value = String.valueOf(animation.getAnimatedValue());
                } else {
                    value = mDecimalFormat.format(animation.getAnimatedValue());
                }
                CountAnimationTextView.super.setText(value);
            }
        });

        mCountAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;

                if (mCountAnimationListener == null) return;
                mCountAnimationListener.onAnimationStart(mCountAnimator.getAnimatedValue());
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;

                if (mCountAnimationListener == null) return;
                mCountAnimationListener.onAnimationEnd(mCountAnimator.getAnimatedValue());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // do nothing
            }
        });
        mCountAnimator.setDuration(DEFAULT_DURATION);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCountAnimator != null) {
            mCountAnimator.cancel();
        }
    }

    public void countAnimation(float fromValue, float toValue) {
        if (isAnimating) return;
        mCountAnimator.setFloatValues(fromValue, toValue);
        mCountAnimator.start();
    }

    public CountAnimationTextView setAnimationDuration(long duration) {
        mCountAnimator.setDuration(duration);
        return this;
    }

    public CountAnimationTextView setInterpolator(@NonNull TimeInterpolator value) {
        mCountAnimator.setInterpolator(value);
        return this;
    }


    // interface progress animationListener
    public interface CountAnimationListener {
        void onAnimationStart(Object animatedValue);

        void onAnimationEnd(Object animatedValue);
    }

    public CountAnimationTextView setDecimalFormat(DecimalFormat mDecimalFormat) {
        this.mDecimalFormat = mDecimalFormat;
        return this;
    }

    public void clearDecimalFormat() {
        this.mDecimalFormat = null;
    }

    public CountAnimationTextView setCountAnimationListener(CountAnimationListener mCountAnimationListener) {
        this.mCountAnimationListener = mCountAnimationListener;
        return this;
    }
}
