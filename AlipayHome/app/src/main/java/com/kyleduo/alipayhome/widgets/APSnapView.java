package com.kyleduo.alipayhome.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import com.kyleduo.alipayhome.widgets.support.ATMathUtils;
import com.kyleduo.alipayhome.widgets.support.ATViewOffsetHelper;

import java.lang.ref.WeakReference;

/**
 *
 * Created by kyleduo on 2017/7/12.
 */

public class APSnapView extends RelativeLayout {

    private APHeaderView.OnOffsetChangeListener mOnOffsetChangeListener;

    private View mContent;
    private ATViewOffsetHelper mOffsetHelper;

    public APSnapView(Context context) {
        super(context);
    }

    public APSnapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public APSnapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = getChildAt(0);
        mOffsetHelper = new ATViewOffsetHelper(mContent);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent != null && parent instanceof APHeaderView) {
            APHeaderView header = (APHeaderView) parent;
            if (mOnOffsetChangeListener == null) {
                mOnOffsetChangeListener = new OffsetChangeListener(this);
            }
            header.addOnOffsetChangeListener(mOnOffsetChangeListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewParent parent = getParent();
        if (parent != null && parent instanceof APHeaderView) {
            APHeaderView header = (APHeaderView) parent;
            if (mOnOffsetChangeListener != null) {
                header.removeOnOffsetChangeListener(mOnOffsetChangeListener);
            }
        }
    }

    void offset(int offset) {
        final float ratio = 0.5f;
        float alpha = 1 - Math.abs(offset) * 1.f / (0.8f * getHeight());
        alpha = ATMathUtils.constrain(alpha, 0, 1);
        if (offset < -getHeight()) {
            offset = -getHeight();
        } else if (offset > 0) {
            offset = 0;
        }
        int value = (int) (-offset * ratio);
        mOffsetHelper.setTopAndBottomOffset(value);
        getChildAt(0).setAlpha(alpha);
    }

    private static class OffsetChangeListener implements APHeaderView.OnOffsetChangeListener {

        private WeakReference<APSnapView> mSnapViewRef;

        public OffsetChangeListener(APSnapView snapView) {
            mSnapViewRef = new WeakReference<>(snapView);
        }

        @Override
        public void onOffsetChanged(APHeaderView header, int currOffset) {
            APSnapView snapView = mSnapViewRef.get();
            if (snapView != null) {
                snapView.offset(currOffset);
            }
        }
    }
}
