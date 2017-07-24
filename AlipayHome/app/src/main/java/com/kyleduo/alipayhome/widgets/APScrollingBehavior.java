package com.kyleduo.alipayhome.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.kyleduo.alipayhome.widgets.support.ATHeaderScrollingViewBehavior;
import com.kyleduo.alipayhome.widgets.support.ATViewOffsetHelper;

import java.util.List;

/**
 *
 * Created by kyleduo on 2017/7/12.
 */

public class APScrollingBehavior extends ATHeaderScrollingViewBehavior {

    ATViewOffsetHelper mOffsetHelper;

    public APScrollingBehavior() {
    }

    public APScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected APHeaderView findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof APHeaderView) {
                return (APHeaderView) view;
            }
        }
        return null;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof APHeaderView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, final View child, View dependency) {
        APHeaderView header = findFirstDependency(parent.getDependencies(child));
        if (header != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) header.getLayoutParams();
            CoordinatorLayout.Behavior behavior = lp.getBehavior();
            if (behavior instanceof APHeaderView.Behavior) {
                APHeaderView.Behavior headerBehavior = (APHeaderView.Behavior) behavior;
                int offset = headerBehavior.getTopAndBottomOffset();
                ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop()));
            }
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    protected int getScrollRange(View v) {
        if (v instanceof APHeaderView) {
            return ((APHeaderView) v).getScrollRange();
        }
        return super.getScrollRange(v);
    }

    @Override
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout coordinatorLayout, View child, Rect rectangle, boolean immediate) {
        final APHeaderView header = findFirstDependency(coordinatorLayout.getDependencies(child));
        if (header != null) {
            // Offset the rect by the child's left/top
            rectangle.offset(child.getLeft(), child.getTop());

            final Rect parentRect = mTempRect1;
            parentRect.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());

            if (!parentRect.contains(rectangle)) {
                // If the rectangle can not be fully seen the visible bounds, collapse
                // the AppBarLayout
                header.setExpanded(false);
                return true;
            }
        }
        return false;
    }
}
