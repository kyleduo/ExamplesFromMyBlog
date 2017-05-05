package com.kyleduo.digging.translucentstatusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.MenuItem;

import butterknife.BindView;

/**
 * for DiggingTranslucentStatusBar
 * Created by kyleduo on 2017/5/5.
 */

public class Demo2Activity extends BaseActivity {

    @BindView(R.id.main_appbar)
    AppBarLayout mAppBarLayout;


    @Override
    protected int getLayoutResId() {
        return R.layout.act_demo2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -mToolbar.getHeight() && mToolbar.getTranslationY() == 0) {
                    mToolbar.animate().translationY(dp2px(-16)).setDuration(100).start();
                } else if (verticalOffset > -mToolbar.getHeight() + mToolbar.getTop() && mToolbar.getTranslationY() == dp2px(-16)) {
                    mToolbar.animate().translationY(0).setDuration(100).start();
                }
            }
        });

    }

    private int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
