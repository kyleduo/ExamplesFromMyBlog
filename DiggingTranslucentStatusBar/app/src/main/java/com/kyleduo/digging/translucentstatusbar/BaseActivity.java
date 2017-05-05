package com.kyleduo.digging.translucentstatusbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * for DiggingTranslucentStatusBar
 * Created by kyleduo on 2017/5/5.
 */

public class BaseActivity extends AppCompatActivity {
    private static final int INVALID_RES_ID = 0;

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resId = getLayoutResId();
        if (resId == INVALID_RES_ID) {
            return;
        }
        setContentView(resId);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //noinspection deprecation
            actionBar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
        }

    }

    protected int getLayoutResId() {
        return INVALID_RES_ID;
    }
}
