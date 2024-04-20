package com.debamalya.shopapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class HidableLayout extends LinearLayout {

    private boolean isVisible = true;
    private final int animationDuration = 300;

    public HidableLayout(Context context) {
        super(context);
    }

    public HidableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HidableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void hide() {
        if (isVisible) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
            animator.setDuration(animationDuration);
            animator.start();
            isVisible = false;
            setVisibility(View.GONE);
        }
    }

    public void show() {
        if (!isVisible) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
            animator.setDuration(animationDuration);
            animator.start();
            isVisible = true;
            setVisibility(View.VISIBLE);
        }
    }
}


