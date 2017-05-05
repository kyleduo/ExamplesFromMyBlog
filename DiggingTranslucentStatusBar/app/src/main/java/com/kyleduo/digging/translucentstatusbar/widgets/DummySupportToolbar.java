package com.kyleduo.digging.translucentstatusbar.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by kyleduo on 2017/5/3.
 */

public class DummySupportToolbar extends Toolbar {

    private View mDummyView;

    public DummySupportToolbar(Context context) {
        super(context);
    }

    public DummySupportToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DummySupportToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (View.class.isInstance(child)) {
            mDummyView = child;
            if (height == ViewGroup.LayoutParams.MATCH_PARENT) {
                height = getSuggestedMinimumHeight();
            }
        }
        super.addView(child, width, height);
    }
}
