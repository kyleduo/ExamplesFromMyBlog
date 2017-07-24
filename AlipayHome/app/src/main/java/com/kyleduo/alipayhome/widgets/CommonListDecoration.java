package com.kyleduo.alipayhome.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by kyleduo on 2017/6/30.
 */

public class CommonListDecoration extends RecyclerView.ItemDecoration {

    private int mSpaceV, mSpaceH;

    public CommonListDecoration() {
    }

    public CommonListDecoration(int spaceV, int spaceH) {
        mSpaceV = spaceV;
        mSpaceH = spaceH;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();
        int itemCount = adapter == null ? 0 : adapter.getItemCount();

        if (mSpaceH == 0 && mSpaceV == 0) {
            float density = parent.getContext().getResources().getDisplayMetrics().density;
            mSpaceH = (int) (density * 16);
            mSpaceV = (int) (density * 12);
        }
        //noinspection UnnecessaryLocalVariable
        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = 0;

        if (position == 0) {
            top = mSpaceV;
            bottom = mSpaceV / 2;
        } else if (position == itemCount - 1) {
            top = mSpaceV / 2;
            bottom = mSpaceV;
        } else {
            top = mSpaceV / 2;
            bottom = mSpaceV / 2;
        }

        left = mSpaceH;
        right = mSpaceH;

        outRect.set(left, top, right, bottom);
    }
}
