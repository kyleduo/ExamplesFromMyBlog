package com.kyleduo.alipayhome.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.kyleduo.alipayhome.R;
import com.kyleduo.alipayhome.widgets.support.ATHeaderBehavior;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by kyleduo on 2017/7/11.
 */
@CoordinatorLayout.DefaultBehavior(APHeaderView.Behavior.class)
public class APHeaderView extends ViewGroup {
    private static final int PENDING_ACTION_COLLAPSED = 0x0001;
    private static final int PENDING_ACTION_EXPANDED = 0x0010;
    private static final int PENDING_ACTION_ANIMATED = 0x0010;

    private View mBar;
    private View mSnapView;
    private List<View> mScrollableViews;
    private List<OnOffsetChangeListener> mOnOffsetChangeListeners;
    private OnHeaderFlingUnConsumedListener mOnHeaderFlingUnConsumedListener;

    private int mPendingAction;

    public APHeaderView(@NonNull Context context) {
        this(context, null);
    }

    public APHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public View getBar() {
        return mBar;
    }

    public View getSnapView() {
        return mSnapView;
    }

    public List<View> getScrollableViews() {
        return mScrollableViews;
    }

    public int getScrollRange() {
        int range = mSnapView.getMeasuredHeight();
        if (mScrollableViews != null) {
            for (View sv : mScrollableViews) {
                range += sv.getMeasuredHeight();
            }
        }
        return range;
    }

    private int getSnapRange() {
        return mSnapView.getHeight();
    }

    /**
     * 自动收起snapView的offset阈值
     *
     * @return
     */
    private int getCollapseSnapOffset() {
        return mSnapView.getHeight();
    }

    public Behavior getBehavior() {
        LayoutParams lp = getLayoutParams();
        if (lp != null && lp instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams clp = (CoordinatorLayout.LayoutParams) lp;
            CoordinatorLayout.Behavior b = clp.getBehavior();
            if (b instanceof Behavior) {
                return (Behavior) b;
            }
            return null;
        }
        return null;
    }

    public void setExpanded(boolean expanded) {
        mPendingAction = expanded ? PENDING_ACTION_EXPANDED : PENDING_ACTION_COLLAPSED;
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        if (childCount < 2) {
            throw new IllegalStateException("Child count must >= 2");
        }
        mBar = findViewById(R.id.alipay_bar);
        mSnapView = findViewById(R.id.alipay_snap);
        mScrollableViews = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (v != mBar && v != mSnapView) {
                mScrollableViews.add(v);
            }
        }
        mBar.bringToFront();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSize == 0) {
            heightSize = Integer.MAX_VALUE;
        }

        int height = 0;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View c = getChildAt(i);
            measureChildWithMargins(
                    c,
                    MeasureSpec.makeMeasureSpec(widthSize - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    0,
                    MeasureSpec.makeMeasureSpec(heightSize - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST),
                    height
            );
            height += c.getMeasuredHeight();
        }

        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(
                widthSize,
                height
        );
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childTop = getPaddingTop();
        int childLeft = getPaddingLeft();
        mBar.layout(childLeft, childTop, childLeft + mBar.getMeasuredWidth(), childTop + mBar.getMeasuredHeight());
        childTop += mBar.getMeasuredHeight();

        mSnapView.layout(childLeft, childTop, childLeft + mSnapView.getMeasuredWidth(), childTop + mSnapView.getMeasuredHeight());

        childTop += mSnapView.getMeasuredHeight();

        for (View sv : mScrollableViews) {
            sv.layout(childLeft, childTop, childLeft + sv.getMeasuredWidth(), childTop + sv.getMeasuredHeight());
            childTop += sv.getMeasuredHeight();
        }
    }

    public void addOnOffsetChangeListener(OnOffsetChangeListener listener) {
        if (mOnOffsetChangeListeners == null) {
            mOnOffsetChangeListeners = new ArrayList<>();
        }
        if (mOnOffsetChangeListeners.contains(listener)) {
            return;
        }
        mOnOffsetChangeListeners.add(listener);
    }

    public void removeOnOffsetChangeListener(OnOffsetChangeListener listener) {
        if (mOnOffsetChangeListeners == null || mOnOffsetChangeListeners.size() == 0) {
            return;
        }
        if (mOnOffsetChangeListeners.contains(listener)) {
            mOnOffsetChangeListeners.remove(listener);
        }
    }

    private void dispatchOffsetChange(int offset) {
        if (mOnOffsetChangeListeners != null) {
            for (OnOffsetChangeListener listener : mOnOffsetChangeListeners) {
                listener.onOffsetChanged(this, offset);
            }
        }
    }

    public void setOnHeaderFlingUnConsumedListener(OnHeaderFlingUnConsumedListener onHeaderFlingUnConsumedListener) {
        mOnHeaderFlingUnConsumedListener = onHeaderFlingUnConsumedListener;
    }

    public interface OnOffsetChangeListener {
        void onOffsetChanged(APHeaderView header, int currOffset);
    }

    public interface OnHeaderFlingUnConsumedListener {
        int onFlingUnConsumed(APHeaderView header, int targetOffset, int unconsumed);
    }

    public static class Behavior extends ATHeaderBehavior<APHeaderView> {
        private ValueAnimator mOffsetAnimator;
        private boolean mSkipNestedPreScroll;
        private WeakReference<View> mLastNestedScrollingChildRef;

        private boolean mWasFlung;
        private boolean mShouldDispatchFling;

        private int mTempFlingDispatchConsumed;
        private int mTempFlingMinOffset;
        private int mTempFlingMaxOffset;

        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean canDragView(APHeaderView view) {
            if (mLastNestedScrollingChildRef != null) {
                View scrollable = mLastNestedScrollingChildRef.get();
                return scrollable != null
                        && scrollable.isShown()
                        && !ViewCompat.canScrollVertically(scrollable, -1)
                        && getTopAndBottomOffset() > -view.getScrollRange()
                        ;
            }
            return true;
        }

        @Override
        protected int getMaxDragOffset(APHeaderView view) {
            return -view.getScrollRange();
        }

        @Override
        protected int getScrollRangeForDragFling(APHeaderView view) {
            return view.getScrollRange();
        }

        @Override
        public int setHeaderTopBottomOffset(CoordinatorLayout parent, APHeaderView header, int newOffset, int minOffset, int maxOffset) {
            final int curOffset = getTopAndBottomOffset();
            final int min;
            final int max;
            if (mShouldDispatchFling) {
                min = Math.max(mTempFlingMinOffset, minOffset);
                max = Math.min(mTempFlingMaxOffset, maxOffset);
            } else {
                min = minOffset;
                max = maxOffset;
            }

            int consumed = super.setHeaderTopBottomOffset(parent, header, newOffset, min, max);
            // consumed 的符号和 dy 相反

            header.dispatchOffsetChange(getTopAndBottomOffset());

            int delta = 0;

            if (mShouldDispatchFling && header.mOnHeaderFlingUnConsumedListener != null) {
                int unconsumedY = newOffset - curOffset + consumed - mTempFlingDispatchConsumed;
                if (unconsumedY != 0) {
                    delta = header.mOnHeaderFlingUnConsumedListener.onFlingUnConsumed(header, newOffset, unconsumedY);
                }
                mTempFlingDispatchConsumed += -delta;
            }

            return consumed + delta;
        }

        @Override
        protected boolean fling(CoordinatorLayout coordinatorLayout, APHeaderView layout, int minOffset, int maxOffset, float velocityY) {
            int min = minOffset;
            int max = maxOffset;
            if (velocityY < 0) {
                // 向上滚动
                mShouldDispatchFling = true;
                mTempFlingDispatchConsumed = 0;
                mTempFlingMinOffset = minOffset;
                mTempFlingMaxOffset = maxOffset;
                min = Integer.MIN_VALUE;
                max = Integer.MAX_VALUE;
            }
            return super.fling(coordinatorLayout, layout, min, max, velocityY);
        }

        @Override
        protected void onFlingFinished(CoordinatorLayout parent, APHeaderView header) {
            mShouldDispatchFling = false;
            checkSnap(parent, header);
        }

        public boolean checkSnap(CoordinatorLayout parent, APHeaderView header) {
            int offset = getTopAndBottomOffset();
            int snapRange = header.getSnapRange();
            int snapCollapsedOffset = -snapRange;
            int snapExpandedOffset = 0;
            if (offset > snapCollapsedOffset && offset < snapExpandedOffset) {
                int target = offset < (snapExpandedOffset + snapCollapsedOffset) / 2 ? snapCollapsedOffset : snapExpandedOffset;
                animateOffsetTo(parent, header, target, 0);
                return true;
            }
            return false;
        }

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, APHeaderView child, int layoutDirection) {
            boolean handled = super.onLayoutChild(parent, child, layoutDirection);

            if (child.mPendingAction != 0) {
                if ((child.mPendingAction & PENDING_ACTION_COLLAPSED) != 0) {
                    setHeaderTopBottomOffset(parent, child, -child.getScrollRange());
                } else if ((child.mPendingAction & PENDING_ACTION_EXPANDED) != 0) {
                    setHeaderTopBottomOffset(parent, child, 0);
                }
            }

            child.dispatchOffsetChange(getTopAndBottomOffset());
            return handled;
        }


        private void animateOffsetTo(final CoordinatorLayout coordinatorLayout,
                                     final APHeaderView child, final int offset, float velocity) {
            final int distance = Math.abs(getTopBottomOffsetForScrollingSibling() - offset);

            int duration;
            velocity = Math.abs(velocity);
            if (velocity > 0) {
                duration = Math.round(1000 * (distance / velocity));
            } else {
                final float distanceRatio = (float) distance / child.getHeight();
                duration = (int) ((distanceRatio + 1) * 150);
            }

            animateToOffset(coordinatorLayout, child, offset, duration);
        }

        private void animateToOffset(final CoordinatorLayout parent, final APHeaderView header, final int target, int duration) {
            final int current = getTopAndBottomOffset();
            if (current == target) {
                if (mOffsetAnimator != null) {
                    mOffsetAnimator.cancel();
                }
            }
            if (mOffsetAnimator == null) {
                mOffsetAnimator = new ValueAnimator();
                mOffsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setHeaderTopBottomOffset(parent, header, (Integer) animation.getAnimatedValue());
                    }
                });
            } else {
                mOffsetAnimator.cancel();
            }
            mOffsetAnimator.setDuration(duration);
            mOffsetAnimator.setIntValues(current, target);
            mOffsetAnimator.start();
        }

        @Override
        protected void onStartDragging() {
            super.onStartDragging();
            if (mOffsetAnimator != null) {
                mOffsetAnimator.cancel();
            }
            mShouldDispatchFling = false;
            stopFling();
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, APHeaderView child, View directTargetChild, View target, int nestedScrollAxes) {
            boolean start = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) > 0
                    && child.getScrollRange() > 0
                    && coordinatorLayout.getHeight() - directTargetChild.getHeight() <= child.getHeight();
            if (start && mOffsetAnimator != null) {
                mOffsetAnimator.cancel();
            }
            mLastNestedScrollingChildRef = null;
            mWasFlung = false;
            mShouldDispatchFling = false;
            stopFling();
            return start;
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, APHeaderView child, View target, int dx, int dy, int[] consumed) {
            if (dy > 0 && !mSkipNestedPreScroll) {
                int min, max;
                min = -child.getScrollRange();
                max = 0;
                consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
            }
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout, APHeaderView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            if (dyUnconsumed < 0) {
                scroll(coordinatorLayout, child, dyUnconsumed, -child.getScrollRange(), 0);
                mSkipNestedPreScroll = true;
            } else {
                mSkipNestedPreScroll = false;
            }
        }

        @Override
        public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, APHeaderView child, View target) {
            if (!mWasFlung) {
                checkSnap(coordinatorLayout, child);
            }
            mSkipNestedPreScroll = false;
            mLastNestedScrollingChildRef = new WeakReference<>(target);
        }

        @Override
        public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, APHeaderView child, View target, float velocityX, float velocityY) {
            if (velocityY > 0 && getTopAndBottomOffset() > -child.getScrollRange()) {
                fling(coordinatorLayout, child, -child.getScrollRange(), 0, -velocityY);
                mWasFlung = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, APHeaderView child, View target, float velocityX, float velocityY, boolean consumed) {
            mWasFlung = true;
            return false;
        }
    }
}

