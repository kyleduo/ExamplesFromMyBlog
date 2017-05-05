package com.kyleduo.digging.translucentstatusbar.widgets;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * Created by kyleduo on 2017/5/5.
 */

public class FitCollapsingToolbarLayout extends CollapsingToolbarLayout {

    Field mLastInsetsField;

    public FitCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public FitCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        try {
            mLastInsetsField = CollapsingToolbarLayout.class.getDeclaredField("mLastInsets");
            mLastInsetsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mLastInsetsField != null) {
            try {
                WindowInsetsCompat insetsCompat = (WindowInsetsCompat) mLastInsetsField.get(this);
                if (insetsCompat != null && insetsCompat.getSystemWindowInsetTop() > 0) {
                    setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() + insetsCompat.getSystemWindowInsetTop());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
