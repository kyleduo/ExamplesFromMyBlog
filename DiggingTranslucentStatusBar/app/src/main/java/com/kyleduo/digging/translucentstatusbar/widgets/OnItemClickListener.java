package com.kyleduo.digging.translucentstatusbar.widgets;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kyleduo on 2017/4/27.
 */

public interface OnItemClickListener {
    void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
}
